package problems.vendingmachine;

import java.util.HashMap;
import java.util.Map;

class Inventory {
    private static final class Slot {
        final Product product;
        int quantity;

        Slot(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    private final Map<String, Slot> slots = new HashMap<>();

    void stock(String code, Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        slots.merge(code, new Slot(product, quantity), (old, added) -> {
            old.quantity += added.quantity;
            return old;
        });
    }

    boolean exists(String code) {
        return slots.containsKey(code);
    }

    boolean available(String code) {
        Slot s = slots.get(code);
        return s != null && s.quantity > 0;
    }

    Product product(String code) {
        return slots.get(code).product;
    }

    void take(String code) {
        slots.get(code).quantity--;
    }

    int quantity(String code) {
        Slot s = slots.get(code);
        return s == null ? 0 : s.quantity;
    }
}
