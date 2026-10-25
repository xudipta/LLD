# Parking lot: from counters to strategies, then thread safety

Final design and trade-offs: [problems/parkinglot](../../src/main/java/problems/parkinglot/README.md).
Format of this walkthrough: [README](README.md).

The same scenarios in [`BehaviourTest`](../../src/test/java/walkthroughs/parkinglot/BehaviourTest.java) run against step 1,
step 2 and the final `problems.parkinglot` implementation: trucks need large spots; small vehicles overflow into bigger
spots but not the other way; leaving frees the spot; a one-hour minimum fee per vehicle type.

---

## Step 1: it works
[`step1/ParkingLot.java`](../../src/main/java/walkthroughs/parkinglot/step1/ParkingLot.java)

Three counters (`small`, `medium`, `large`), vehicle types as strings, a `HashMap<String, String[]>` of tickets, fees as an
if/else chain on the type, and `System.currentTimeMillis()` read directly.

### Smells
1. **Stringly typed.** `"car"` vs `"Car"`, and an unknown type silently finds no spot.
2. **The wall clock is hard-wired**, so a fee for "61 minutes" cannot be tested without waiting an hour.
3. **A `String[]` as a record.** `t[0]`, `t[1]` and `t[2]` mean type, spot and time; nothing says so.
4. **Pricing is an if/else in `unpark`.** A new price plan edits the same method that frees spots.
5. **No validation.** An unknown ticket is a `NullPointerException`
   (`step1CrashesOnAnUnknownTicket` pins this behaviour down).
6. **The allocation rules are a ladder of nested ifs**, duplicated in the counters.

### Refactoring
*Replace type code with an enum*; *replace conditional with strategy* (pricing); *inject the clock*; *introduce a record*
for what a ticket holds; *validate input*.

---

## Step 2: strategies and a clock
[`step2/`](../../src/main/java/walkthroughs/parkinglot/step2)

`VehicleType` is an enum that knows the smallest spot it fits. `PricingStrategy` / `HourlyPricing` computes the fee from a
`Duration`. `ParkingLot` takes a `Clock`, uses a private `Parked` record for each ticket, picks the smallest spot that fits
with a simple loop, and rejects unknown tickets with `IllegalArgumentException`.

**New in this step:** fees follow the clock (`feesFollowTheClock`) and unknown tickets are rejected
(`anUnknownTicketIsRejected`). Neither test can be written for step 1.

### Smells that remain
1. **Not thread-safe.** Two entry gates can both see the last free spot and both take it.
2. **Spots are counters, not objects**, so you cannot say *which* spot a vehicle got, add per-spot attributes (electric
   charging, disabled access) or choose between spots by distance to the entrance.
3. **Allocation is fixed in the loop**; "nearest the entrance" versus "keep big spots free" cannot be swapped.

### Refactoring
Make spots objects, extract `SpotAllocationStrategy`, and make "pick a spot and occupy it" **one atomic step** under a lock.

---

## Final: `problems.parkinglot`
[`problems/parkinglot`](../../src/main/java/problems/parkinglot)

`ParkingSpot` objects, `SpotAllocationStrategy` (first fit, best fit), `PricingStrategy`, and a lock around
choose-and-occupy. The final design passes the same behaviour scenarios as steps 1 and 2.

For the concurrency half of this story, see the paired unsafe and safe versions and the race test described in the
[concurrency notes](../concurrency.md): `NaiveParkingLot` (check-then-act, double-books spots) and `ParkingLot` (fixed).

### What a reader should take away
- Step 1 to step 2 is about **testability and naming**: enums, a clock, strategies.
- Step 2 to final is about **modelling and concurrency**: spots as objects, pluggable allocation, atomic claiming.
- The scenarios that stayed green the whole way are the specification; the tests added along the way are the benefit
  of each refactoring.
