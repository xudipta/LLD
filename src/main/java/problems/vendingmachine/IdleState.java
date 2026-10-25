package problems.vendingmachine;

import java.util.List;

class IdleState implements VendingState {
    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addToBalance(coin.cents());
        machine.setState(new HasMoneyState());
    }

    @Override
    public Dispensed select(VendingMachine machine, String code) {
        throw new IllegalStateException("Insert money first");
    }

    @Override
    public List<Coin> cancel(VendingMachine machine) {
        return List.of();
    }
}
