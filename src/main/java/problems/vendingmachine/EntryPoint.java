package problems.vendingmachine;

public class EntryPoint {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        machine.stock("A1", new Product("Chips", 65), 5);
        machine.insertCoin(Coin.DOLLAR);
        Dispensed result = machine.select("A1");
        System.out.println(result.product().name() + ", change: " + result.change());
    }
}
