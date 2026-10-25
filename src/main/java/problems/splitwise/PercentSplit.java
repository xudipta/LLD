package problems.splitwise;

import java.util.LinkedHashMap;
import java.util.Map;

/** Whole-number percentages that must add up to 100; rounding leftovers go to the first participant. */
public class PercentSplit implements SplitStrategy {
    private final Map<String, Integer> percents;

    public PercentSplit(Map<String, Integer> percents) {
        int sum = percents.values().stream().mapToInt(Integer::intValue).sum();
        if (sum != 100) {
            throw new IllegalArgumentException("Percentages add up to " + sum + ", expected 100");
        }
        this.percents = new LinkedHashMap<>(percents);
    }

    @Override
    public Map<String, Long> split(long totalCents) {
        Map<String, Long> shares = new LinkedHashMap<>();
        long assigned = 0;
        for (Map.Entry<String, Integer> e : percents.entrySet()) {
            long share = totalCents * e.getValue() / 100;
            shares.put(e.getKey(), share);
            assigned += share;
        }
        String first = shares.keySet().iterator().next();
        shares.merge(first, totalCents - assigned, Long::sum);
        return shares;
    }
}
