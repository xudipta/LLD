package problems.vendingmachine;

import java.util.List;

class HasMoneyState implements VendingState {
    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addToBalance(coin.cents());
    }

    @Override
    public Dispensed select(VendingMachine machine, String code) {
        Inventory inventory = machine.inventory();
        if (!inventory.exists(code)) {
            throw new IllegalArgumentException("Unknown code: " + code);
        }
        if (!inventory.available(code)) {
            throw new IllegalStateException("Sold out: " + code);
        }
        Product product = inventory.product(code);
        int shortBy = product.priceCents() - machine.balance();
        if (shortBy > 0) {
            throw new IllegalStateException("Insert " + shortBy + " more cents");
        }
        inventory.take(code);
        List<Coin> change = machine.makeChange(machine.balance() - product.priceCents());
        machine.clearBalance();
        machine.setState(new IdleState());
        return new Dispensed(product, change);
    }

    @Override
    public List<Coin> cancel(VendingMachine machine) {
        List<Coin> refund = machine.makeChange(machine.balance());
        machine.clearBalance();
        machine.setState(new IdleState());
        return refund;
    }
}
