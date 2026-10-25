package problems.splitwise;

import java.util.Map;

/** Decides how much of an expense each participant owes. Amounts are in cents and must add up to the total. */
public interface SplitStrategy {
    Map<String, Long> split(long totalCents);
}
