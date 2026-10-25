package walkthroughs.parkinglot.step1;

import java.util.HashMap;
import java.util.Map;

/**
 * STEP 1 - everything in one class: string vehicle types, prices as if/else, the wall clock read
 * directly, and counters for spots. See docs/walkthroughs/parkinglot.md for the smells.
 */
public class ParkingLot {
    private int small;
    private int medium;
    private int large;
    private final Map<String, String[]> tickets = new HashMap<>();   // ticket -> {type, spot size, start millis}
    private int next = 1;

    public ParkingLot(int small, int medium, int large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    /** @return a ticket id, or null if there is no room */
    public String park(String type) {
        String spot = null;
        if (type.equals("bike")) {
            if (small > 0) { small--; spot = "small"; }
            else if (medium > 0) { medium--; spot = "medium"; }
            else if (large > 0) { large--; spot = "large"; }
        } else if (type.equals("car")) {
            if (medium > 0) { medium--; spot = "medium"; }
            else if (large > 0) { large--; spot = "large"; }
        } else if (type.equals("truck")) {
            if (large > 0) { large--; spot = "large"; }
        }
        if (spot == null) return null;
        String id = "T-" + next++;
        tickets.put(id, new String[]{type, spot, String.valueOf(System.currentTimeMillis())});
        return id;
    }

    public long unpark(String ticket) {
        String[] t = tickets.remove(ticket);
        if (t[1].equals("small")) small++;
        else if (t[1].equals("medium")) medium++;
        else large++;
        long millis = System.currentTimeMillis() - Long.parseLong(t[2]);
        long hours = Math.max(1, (millis + 3_599_999) / 3_600_000);
        if (t[0].equals("bike")) return hours * 10;
        else if (t[0].equals("car")) return hours * 20;
        else return hours * 40;
    }
}
