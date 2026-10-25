# Concurrency notes

Three problems in this repo ship a deliberately unsafe version next to the safe one, so you
can see the bug and the fix:

| Problem | Unsafe class | What goes wrong | Safe class | Fix |
|---|---|---|---|---|
| [LRU cache](../src/main/java/problems/lrucache/README.md) | `NaiveLruCache` | `get` reorders a shared list; concurrent calls corrupt it (exceptions, broken links, size over capacity) | `ConcurrentLruCache` | One `ReentrantLock` around every operation |
| [Rate limiter](../src/main/java/problems/ratelimiter/README.md) | `NaiveTokenBucketRateLimiter` | Check-then-act: many threads see a token before any takes it, so more calls are admitted than the limit | `TokenBucketRateLimiter` | `synchronized` on the per-key bucket |
| [Parking lot](../src/main/java/problems/parkinglot/README.md) | `NaiveParkingLot` | Check-then-act: two vehicles are given the same free spot | `ParkingLot` | One lock around "choose a spot and occupy it" |

## The bug is almost always check-then-act
`if (free) { take }` is two steps. Any thread can run between them. The fix is to make the
check and the action one atomic step: a lock, a compare-and-set, or a single atomic
collection method such as `computeIfAbsent`.

## Choosing a tool
| Tool | Use when | Cost |
|---|---|---|
| `synchronized` | A small critical section, one monitor per object | Simple; blocks other threads; no timeout |
| `ReentrantLock` | You need `tryLock`, timeouts, fairness or multiple conditions | Must unlock in `finally` |
| `ReadWriteLock` | Reads far outnumber writes **and** reads don't mutate | Wasted when reads mutate (see the LRU `get`) |
| Concurrent collections | Independent per-key operations | Only individual methods are atomic, not sequences of them |
| Atomics / CAS | A single variable, or an immutable snapshot swapped in one step | Retry loops; hard to extend to several fields |
| Lock striping | One lock is the bottleneck and work partitions by key | More complex; global invariants get weaker |
| Immutability | State rarely changes | Copy on write |

## Rules the code here follows
1. Guard shared mutable state with **one** lock and write down which one.
2. Keep the critical section as small as correctness allows, but never smaller.
3. Don't call unknown code (callbacks, strategies that block) while holding a lock if you can avoid it.
4. Inject time (`Clock`, `LongSupplier`) so tests don't `sleep`.
5. Test concurrency with a start latch so threads begin together, and assert on invariants (count, uniqueness, consistency), not on timing.

## About the "race" tests
The unsafe versions have a `Thread.yield()` between the check and the act to widen the
window. Even so, a race is probabilistic. Those tests retry up to five times and use
`assumeTrue`, so a lucky run is reported as **skipped**, never as a failure. The tests for
the safe versions assert exact invariants and always run.
