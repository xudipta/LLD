package patterns.facade;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeTheaterFacadeTest {
    private final HomeTheaterFacade theater = new HomeTheaterFacade();

    @Test
    void startsSubsystemsInOrder() {
        assertEquals(List.of("Lights dimmed", "Projector on", "Volume set to 8"), theater.watchMovie());
    }

    @Test
    void shutsDownInReverseOrder() {
        assertEquals(List.of("Amplifier off", "Projector off", "Lights on"), theater.endMovie());
    }
}
