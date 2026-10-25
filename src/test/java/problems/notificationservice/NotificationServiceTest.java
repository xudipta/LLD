package problems.notificationservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {
    /** Records messages and fails the first N attempts, so retries are observable. */
    private static class FakeChannel implements NotificationChannel {
        final Channel channel;
        final List<String> sent = new ArrayList<>();
        int failuresLeft;
        int calls;

        FakeChannel(Channel channel, int failures) {
            this.channel = channel;
            this.failuresLeft = failures;
        }

        @Override public Channel channel() { return channel; }

        @Override
        public void send(String userId, String message) {
            calls++;
            if (failuresLeft-- > 0) throw new DeliveryException(channel + " is down");
            sent.add(userId + ": " + message);
        }
    }

    private FakeChannel email;
    private FakeChannel sms;
    private TemplateRegistry templates;
    private PreferenceStore prefs;

    @BeforeEach
    void setUp() {
        email = new FakeChannel(Channel.EMAIL, 0);
        sms = new FakeChannel(Channel.SMS, 0);
        templates = new TemplateRegistry();
        templates.register("SHIPPED", "Hi {name}, order {id} shipped.");
        prefs = new PreferenceStore();
    }

    private NotificationService service(int attempts, NotificationChannel... channels) {
        return new NotificationService(List.of(channels), templates, prefs, attempts);
    }

    private static final Map<String, String> PARAMS = Map.of("name", "Asha", "id", "A-1");

    @Test
    void sendsTheRenderedMessageOnEveryChannelByDefault() {
        Map<Channel, DeliveryResult> report = service(1, email, sms).send("u1", "SHIPPED", PARAMS);
        assertEquals(List.of("u1: Hi Asha, order A-1 shipped."), email.sent);
        assertEquals(List.of("u1: Hi Asha, order A-1 shipped."), sms.sent);
        assertTrue(report.get(Channel.EMAIL).delivered());
        assertTrue(report.get(Channel.SMS).delivered());
    }

    @Test
    void respectsUserPreferences() {
        prefs.set("u1", "SHIPPED", Set.of(Channel.SMS));
        Map<Channel, DeliveryResult> report = service(1, email, sms).send("u1", "SHIPPED", PARAMS);
        assertEquals(Set.of(Channel.SMS), report.keySet());
        assertTrue(email.sent.isEmpty());
    }

    @Test
    void optingOutSendsNothing() {
        prefs.optOut("u1", "SHIPPED");
        assertTrue(service(1, email, sms).send("u1", "SHIPPED", PARAMS).isEmpty());
        assertEquals(0, email.calls + sms.calls);
    }

    @Test
    void preferencesAreIndividualPerUserAndType() {
        prefs.optOut("u1", "SHIPPED");
        assertFalse(service(1, email).send("u2", "SHIPPED", PARAMS).isEmpty());
    }

    @Test
    void retriesUntilItSucceeds() {
        FakeChannel flaky = new FakeChannel(Channel.EMAIL, 2);
        DeliveryResult r = service(3, flaky).send("u1", "SHIPPED", PARAMS).get(Channel.EMAIL);
        assertTrue(r.delivered());
        assertEquals(3, r.attempts());
        assertEquals(3, flaky.calls);
    }

    @Test
    void givesUpAfterMaxAttemptsAndReportsTheError() {
        FakeChannel down = new FakeChannel(Channel.EMAIL, 99);
        DeliveryResult r = service(3, down).send("u1", "SHIPPED", PARAMS).get(Channel.EMAIL);
        assertFalse(r.delivered());
        assertEquals(3, r.attempts());
        assertEquals("EMAIL is down", r.error());
    }

    @Test
    void aFailingChannelDoesNotStopTheOthers() {
        FakeChannel down = new FakeChannel(Channel.EMAIL, 99);
        Map<Channel, DeliveryResult> report = service(2, down, sms).send("u1", "SHIPPED", PARAMS);
        assertFalse(report.get(Channel.EMAIL).delivered());
        assertTrue(report.get(Channel.SMS).delivered());
        assertEquals(1, sms.sent.size());
    }

    @Test
    void unknownTypeAndMissingParametersFailBeforeAnythingIsSent() {
        NotificationService s = service(1, email);
        assertThrows(IllegalArgumentException.class, () -> s.send("u1", "NOPE", PARAMS));
        assertThrows(IllegalArgumentException.class, () -> s.send("u1", "SHIPPED", Map.of("name", "Asha")));
        assertEquals(0, email.calls);
    }

    @Test
    void paramValuesWithSpecialCharactersAreInsertedLiterally() {
        templates.register("T", "Total: {amount}");
        service(1, email).send("u1", "T", Map.of("amount", "$5 \\ done"));
        assertEquals("u1: Total: $5 \\ done", email.sent.get(0));
    }

    @Test
    void needsAtLeastOneAttempt() {
        assertThrows(IllegalArgumentException.class, () -> service(0, email));
    }

    @Test
    void channelsWithNoSenderAreSkipped() {
        Map<Channel, DeliveryResult> report = service(1, email).send("u1", "SHIPPED", PARAMS);
        assertEquals(Set.of(Channel.EMAIL), report.keySet());
    }
}
