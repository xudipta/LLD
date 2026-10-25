package problems.splitwise;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpenseManager {
    /** owes.get(a).get(b) = cents that a owes b. Kept netted, so only one direction is ever non-zero. */
    private final Map<String, Map<String, Long>> owes = new TreeMap<>();

    public void addExpense(String paidBy, long totalCents, SplitStrategy strategy) {
        if (totalCents <= 0) {
            throw new IllegalArgumentException("Expense must be positive");
        }
        Map<String, Long> shares = strategy.split(totalCents);
        long sum = shares.values().stream().mapToLong(Long::longValue).sum();
        if (sum != totalCents || shares.values().stream().anyMatch(v -> v < 0)) {
            throw new IllegalArgumentException("Shares must be non-negative and add up to the total");
        }
        shares.forEach((person, share) -> {
            if (!person.equals(paidBy) && share > 0) {
                addDebt(person, paidBy, share);
            }
        });
    }

    /** {@code from} pays back {@code to}. */
    public void settle(String from, String to, long cents) {
        if (cents <= 0) {
            throw new IllegalArgumentException("Settlement must be positive");
        }
        if (owed(from, to) < cents) {
            throw new IllegalArgumentException(from + " does not owe " + to + " that much");
        }
        addDebt(to, from, cents);   // paying back is the opposite debt, and nets out
    }

    private void addDebt(String debtor, String creditor, long cents) {
        long reverse = owed(creditor, debtor);
        if (reverse >= cents) {
            setOwed(creditor, debtor, reverse - cents);
        } else {
            setOwed(creditor, debtor, 0);
            setOwed(debtor, creditor, owed(debtor, creditor) + cents - reverse);
        }
    }

    private long owed(String debtor, String creditor) {
        return owes.getOrDefault(debtor, Map.of()).getOrDefault(creditor, 0L);
    }

    private void setOwed(String debtor, String creditor, long cents) {
        if (cents == 0) {
            Map<String, Long> row = owes.get(debtor);
            if (row != null) {
                row.remove(creditor);
                if (row.isEmpty()) owes.remove(debtor);
            }
        } else {
            owes.computeIfAbsent(debtor, k -> new TreeMap<>()).put(creditor, cents);
        }
    }

    /** Who owes whom directly, after netting each pair. */
    public List<Debt> balances() {
        List<Debt> debts = new ArrayList<>();
        owes.forEach((from, row) -> row.forEach((to, cents) -> debts.add(new Debt(from, to, cents))));
        return debts;
    }

    /** Net position of one person: positive means they are owed money. */
    public long netBalance(String person) {
        long net = 0;
        for (Debt d : balances()) {
            if (d.to().equals(person)) net += d.cents();
            if (d.from().equals(person)) net -= d.cents();
        }
        return net;
    }

    /** Fewest payments that settle everyone (greedy: biggest debtor pays biggest creditor). */
    public List<Debt> simplify() {
        Map<String, Long> net = new HashMap<>();
        for (Debt d : balances()) {
            net.merge(d.to(), d.cents(), Long::sum);
            net.merge(d.from(), -d.cents(), Long::sum);
        }
        List<Map.Entry<String, Long>> creditors = new ArrayList<>();
        List<Map.Entry<String, Long>> debtors = new ArrayList<>();
        net.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e -> {
            if (e.getValue() > 0) creditors.add(new java.util.AbstractMap.SimpleEntry<>(e));
            if (e.getValue() < 0) debtors.add(new java.util.AbstractMap.SimpleEntry<>(e.getKey(), -e.getValue()));
        });
        Comparator<Map.Entry<String, Long>> biggestFirst = Map.Entry.<String, Long>comparingByValue().reversed();
        List<Debt> payments = new ArrayList<>();
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            creditors.sort(biggestFirst);
            debtors.sort(biggestFirst);
            Map.Entry<String, Long> creditor = creditors.get(0);
            Map.Entry<String, Long> debtor = debtors.get(0);
            long amount = Math.min(creditor.getValue(), debtor.getValue());
            payments.add(new Debt(debtor.getKey(), creditor.getKey(), amount));
            creditor.setValue(creditor.getValue() - amount);
            debtor.setValue(debtor.getValue() - amount);
            if (creditor.getValue() == 0) creditors.remove(0);
            if (debtor.getValue() == 0) debtors.remove(0);
        }
        return payments;
    }
}
