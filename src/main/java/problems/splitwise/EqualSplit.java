package problems.splitwise;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Equal shares; leftover cents go one each to the first participants so the total is exact. */
public class EqualSplit implements SplitStrategy {
    private final List<String> participants;

    public EqualSplit(List<String> participants) {
        if (participants.isEmpty()) {
            throw new IllegalArgumentException("At least one participant is required");
        }
        this.participants = List.copyOf(participants);
    }

    @Override
    public Map<String, Long> split(long totalCents) {
        int n = participants.size();
        long base = totalCents / n;
        long remainder = totalCents % n;
        Map<String, Long> shares = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            shares.put(participants.get(i), base + (i < remainder ? 1 : 0));
        }
        return shares;
    }
}
