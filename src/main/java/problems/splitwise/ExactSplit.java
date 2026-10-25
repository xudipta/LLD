package problems.splitwise;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExactSplit implements SplitStrategy {
    private final Map<String, Long> amounts;

    public ExactSplit(Map<String, Long> amounts) {
        this.amounts = new LinkedHashMap<>(amounts);
    }

    @Override
    public Map<String, Long> split(long totalCents) {
        long sum = amounts.values().stream().mapToLong(Long::longValue).sum();
        if (sum != totalCents) {
            throw new IllegalArgumentException("Amounts add up to " + sum + " but the expense is " + totalCents);
        }
        return new LinkedHashMap<>(amounts);
    }
}
