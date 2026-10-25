package problems.vendingmachine;

public enum Coin {
    NICKEL(5), DIME(10), QUARTER(25), DOLLAR(100);

    private final int cents;

    Coin(int cents) {
        this.cents = cents;
    }

    public int cents() {
        return cents;
    }
}
