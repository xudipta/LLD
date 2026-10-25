package problems.splitwise;

import java.util.List;

public class EntryPoint {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        manager.addExpense("asha", 3000, new EqualSplit(List.of("asha", "ben", "chen")));
        manager.addExpense("ben", 1000, new EqualSplit(List.of("ben", "chen")));
        manager.balances().forEach(d -> System.out.println(d.from() + " owes " + d.to() + " " + d.cents()));
        System.out.println("Simplified: " + manager.simplify());
    }
}
