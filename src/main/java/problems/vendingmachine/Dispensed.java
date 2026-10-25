package problems.vendingmachine;

import java.util.List;

public record Dispensed(Product product, List<Coin> change) {
}
