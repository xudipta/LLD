package problems.vendingmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {
    private VendingMachine machine;

    @BeforeEach
    void setUp() {
        machine = new VendingMachine();
        machine.stock("A1", new Product("Chips", 65), 2);
        machine.stock("B1", new Product("Gum", 30), 1);
    }

    @Test
    void sellsAndReturnsChange() {
        machine.insertCoin(Coin.DOLLAR);
        Dispensed d = machine.select("A1");
        assertEquals("Chips", d.product().name());
        assertEquals(List.of(Coin.QUARTER, Coin.DIME), d.change());
        assertEquals(0, machine.balance());
        assertEquals(1, machine.quantity("A1"));
    }

    @Test
    void exactMoneyGivesNoChange() {
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.NICKEL);
        assertTrue(machine.select("B1").change().isEmpty());
    }

    @Test
    void cannotSelectBeforeInsertingMoney() {
        assertThrows(IllegalStateException.class, () -> machine.select("A1"));
    }

    @Test
    void insufficientMoneyKeepsTheBalanceAndAllowsTopUp() {
        machine.insertCoin(Coin.QUARTER);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> machine.select("A1"));
        assertEquals("Insert 40 more cents", e.getMessage());
        assertEquals(25, machine.balance());
        machine.insertCoin(Coin.DOLLAR);
        assertEquals("Chips", machine.select("A1").product().name());
    }

    @Test
    void unknownCodeIsRejectedAndMoneyKept() {
        machine.insertCoin(Coin.DOLLAR);
        assertThrows(IllegalArgumentException.class, () -> machine.select("Z9"));
        assertEquals(100, machine.balance());
    }

    @Test
    void soldOutItemCannotBeBought() {
        machine.insertCoin(Coin.DOLLAR);
        machine.select("B1");
        machine.insertCoin(Coin.DOLLAR);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> machine.select("B1"));
        assertEquals("Sold out: B1", e.getMessage());
        assertEquals(100, machine.balance());
    }

    @Test
    void cancelRefundsEverything() {
        machine.insertCoin(Coin.DOLLAR);
        machine.insertCoin(Coin.DIME);
        assertEquals(List.of(Coin.DOLLAR, Coin.DIME), machine.cancel());
        assertEquals(0, machine.balance());
        assertThrows(IllegalStateException.class, () -> machine.select("A1"));
    }

    @Test
    void cancelWithNoMoneyIsHarmless() {
        assertTrue(machine.cancel().isEmpty());
    }

    @Test
    void restockingAddsToExistingQuantity() {
        machine.stock("B1", new Product("Gum", 30), 3);
        assertEquals(4, machine.quantity("B1"));
    }

    @Test
    void rejectsNonPositiveStock() {
        assertThrows(IllegalArgumentException.class, () -> machine.stock("C1", new Product("X", 1), 0));
    }
}
