package problems.splitwise;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseManagerTest {
    // ---- strategies ----
    @Test
    void equalSplitGivesLeftoverCentsToTheFirstPeople() {
        Map<String, Long> shares = new EqualSplit(List.of("a", "b", "c")).split(100);
        assertEquals(Map.of("a", 34L, "b", 33L, "c", 33L), shares);
        assertEquals(100, shares.values().stream().mapToLong(Long::longValue).sum());
    }

    @Test
    void exactSplitMustAddUp() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExactSplit(Map.of("a", 40L, "b", 50L)).split(100));
        assertEquals(Map.of("a", 40L, "b", 60L), new ExactSplit(Map.of("a", 40L, "b", 60L)).split(100));
    }

    @Test
    void percentSplitMustTotalOneHundredAndKeepsEveryCent() {
        assertThrows(IllegalArgumentException.class, () -> new PercentSplit(Map.of("a", 50, "b", 40)));
        Map<String, Integer> p = new LinkedHashMap<>();
        p.put("a", 33);
        p.put("b", 33);
        p.put("c", 34);
        Map<String, Long> shares = new PercentSplit(p).split(101);
        assertEquals(101, shares.values().stream().mapToLong(Long::longValue).sum());
    }

    // ---- balances ----
    @Test
    void payerIsOwedByTheOthers() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 300, new EqualSplit(List.of("a", "b", "c")));
        assertEquals(List.of(new Debt("b", "a", 100), new Debt("c", "a", 100)), m.balances());
        assertEquals(200, m.netBalance("a"));
        assertEquals(-100, m.netBalance("b"));
    }

    @Test
    void payersOwnShareIsNotADebt() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 100, new EqualSplit(List.of("a")));
        assertTrue(m.balances().isEmpty());
    }

    @Test
    void debtsBetweenTheSamePairNetOut() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 200, new EqualSplit(List.of("a", "b")));   // b owes a 100
        m.addExpense("b", 300, new EqualSplit(List.of("a", "b")));   // a owes b 150
        assertEquals(List.of(new Debt("a", "b", 50)), m.balances());
    }

    @Test
    void equalAndOppositeDebtsCancelCompletely() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 200, new EqualSplit(List.of("a", "b")));
        m.addExpense("b", 200, new EqualSplit(List.of("a", "b")));
        assertTrue(m.balances().isEmpty());
    }

    @Test
    void settlingReducesTheDebt() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 200, new EqualSplit(List.of("a", "b")));
        m.settle("b", "a", 40);
        assertEquals(List.of(new Debt("b", "a", 60)), m.balances());
        m.settle("b", "a", 60);
        assertTrue(m.balances().isEmpty());
    }

    @Test
    void cannotSettleMoreThanIsOwed() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 200, new EqualSplit(List.of("a", "b")));
        assertThrows(IllegalArgumentException.class, () -> m.settle("b", "a", 101));
        assertThrows(IllegalArgumentException.class, () -> m.settle("a", "b", 1));
    }

    @Test
    void rejectsNonPositiveExpenses() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExpenseManager().addExpense("a", 0, new EqualSplit(List.of("a", "b"))));
    }

    // ---- simplification ----
    @Test
    void simplifyCollapsesAChainOfDebts() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 100, new ExactSplit(Map.of("b", 100L)));   // b owes a 100
        m.addExpense("b", 100, new ExactSplit(Map.of("c", 100L)));   // c owes b 100
        assertEquals(2, m.balances().size());
        assertEquals(List.of(new Debt("c", "a", 100)), m.simplify());
    }

    @Test
    void simplifiedPaymentsCancelEveryonesNetBalance() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 900, new EqualSplit(List.of("a", "b", "c")));
        m.addExpense("b", 300, new EqualSplit(List.of("a", "b", "c")));
        m.addExpense("c", 600, new EqualSplit(List.of("a", "b", "c", "d")));
        Map<String, Long> afterPayments = new java.util.HashMap<>();
        for (String person : List.of("a", "b", "c", "d")) afterPayments.put(person, m.netBalance(person));
        for (Debt payment : m.simplify()) {
            afterPayments.merge(payment.from(), payment.cents(), Long::sum);   // payer's debt shrinks
            afterPayments.merge(payment.to(), -payment.cents(), Long::sum);    // receiver is owed less
        }
        afterPayments.values().forEach(balance -> assertEquals(0L, balance));
    }

    @Test
    void simplifyNeverNeedsMorePaymentsThanPeopleMinusOne() {
        ExpenseManager m = new ExpenseManager();
        m.addExpense("a", 1000, new EqualSplit(List.of("a", "b", "c", "d", "e")));
        m.addExpense("b", 500, new EqualSplit(List.of("a", "b", "c", "d", "e")));
        m.addExpense("c", 250, new EqualSplit(List.of("a", "b", "c")));
        assertTrue(m.simplify().size() <= 4);
    }
}
