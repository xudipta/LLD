package patterns.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void followsTheHappyPath() {
        Order o = new Order();
        assertEquals("NEW", o.getStatus());
        o.pay();
        assertEquals("PAID", o.getStatus());
        o.ship();
        assertEquals("SHIPPED", o.getStatus());
        o.deliver();
        assertEquals("DELIVERED", o.getStatus());
    }

    @Test
    void cannotShipBeforePaying() {
        Order o = new Order();
        IllegalStateException e = assertThrows(IllegalStateException.class, o::ship);
        assertEquals("Cannot ship an order that is NEW", e.getMessage());
    }

    @Test
    void cannotPayTwice() {
        Order o = new Order();
        o.pay();
        assertThrows(IllegalStateException.class, o::pay);
    }

    @Test
    void failedTransitionKeepsTheState() {
        Order o = new Order();
        assertThrows(IllegalStateException.class, o::deliver);
        assertEquals("NEW", o.getStatus());
    }

    @Test
    void deliveredIsTerminal() {
        Order o = new Order();
        o.pay(); o.ship(); o.deliver();
        assertThrows(IllegalStateException.class, o::ship);
    }
}
