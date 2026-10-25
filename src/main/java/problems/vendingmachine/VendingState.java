package problems.vendingmachine;

import java.util.List;

interface VendingState {
    void insertCoin(VendingMachine machine, Coin coin);

    Dispensed select(VendingMachine machine, String code);

    List<Coin> cancel(VendingMachine machine);
}
