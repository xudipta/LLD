package problems.vendingmachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class VendingMachine {
    private final Inventory inventory = new Inventory();
    private VendingState state = new IdleState();
    private int balanceCents;

    public void stock(String code, Product product, int quantity) {
        inventory.stock(code, product, quantity);
    }

    public void insertCoin(Coin coin) {
        state.insertCoin(this, coin);
    }

    /** Dispenses the product and change; throws if the request can't be served (state is unchanged). */
    public Dispensed select(String code) {
        return state.select(this, code);
    }

    /** Returns all inserted money. */
    public List<Coin> cancel() {
        return state.cancel(this);
    }

    public int balance() {
        return balanceCents;
    }

    public int quantity(String code) {
        return inventory.quantity(code);
    }

    Inventory inventory() {
        return inventory;
    }

    void setState(VendingState state) {
        this.state = state;
    }

    void addToBalance(int cents) {
        balanceCents += cents;
    }

    void clearBalance() {
        balanceCents = 0;
    }

    /** Greedy change, largest coin first. Correct for these denominations; assumes unlimited coins. */
    List<Coin> makeChange(int cents) {
        List<Coin> coins = new ArrayList<>();
        Coin[] byValue = Arrays.stream(Coin.values())
                .sorted(Comparator.comparingInt(Coin::cents).reversed())
                .toArray(Coin[]::new);
        for (Coin coin : byValue) {
            while (cents >= coin.cents()) {
                coins.add(coin);
                cents -= coin.cents();
            }
        }
        return coins;
    }
}
