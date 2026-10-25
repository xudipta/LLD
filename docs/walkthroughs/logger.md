# Logger: from if/else to a chain of handlers

Final design and trade-offs: [problems/logger](../../src/main/java/problems/logger/README.md).
Format of this walkthrough: [README](README.md).

All three steps pass the same scenarios in
[`BehaviourTest`](../../src/test/java/walkthroughs/logger/BehaviourTest.java): each of the three levels prints with its
label, and messages come out in order.

---

## Step 1: if/else per level
[`step1/Logger.java`](../../src/main/java/walkthroughs/logger/step1/Logger.java)

```java
if (level == 1)      System.out.println("DEBUG: " + msg);
else if (level == 2) System.out.println("ERROR: " + msg);
else if (level == 3) System.out.println("INFO: "  + msg);
```

### Smells
1. **Magic numbers.** `1`, `2`, `3` appear in the method even though named constants exist right above it.
2. **Closed for extension.** Every new level edits `log`, risking the existing ones.
3. **Hard-wired destination.** `System.out` is fixed. Tests have to capture the process's stdout.
4. **An unknown level is silently dropped**, with no sign anything went wrong.

### Refactoring
*Replace magic numbers with an enum*; *inject the destination* (`PrintStream`).

---

## Step 2: an enum and an injected output
[`step2/`](../../src/main/java/walkthroughs/logger/step2)

`Level` is an enum, and its name is the label, so there is no if/else at all: `out.println(level + ": " + msg)`. The
`PrintStream` is a constructor argument, so a test passes its own.

### Smells that remain
1. **Every level goes to the same place.** To send errors to a file and the rest to the console you must add conditions
   again, bringing back the original problem.
2. No way to add behaviour for a level (alerting on `ERROR`, say) without editing `Logger`.

### Refactoring
*Replace conditional with polymorphism* by chaining handlers, each owning one level and its own sink.

---

## Step 3: Chain of Responsibility
[`step3/LogHandler.java`](../../src/main/java/walkthroughs/logger/step3/LogHandler.java)

Each `LogHandler` owns a level and a destination. If the message is its level it prints; otherwise it passes the message
to `next`. The chain is wired once at start-up.

**New in this step:** each level can go to a different destination, with no change to the logger code
(`step3CanSendEachLevelToItsOwnDestination`).

### Trade-off to be honest about
For the simple "one level, one destination" behaviour, step 2 is shorter and perfectly good. The chain earns its keep when
destinations or behaviours differ per level, or when more than one handler might act on a message. See
[Chain of Responsibility](../../src/main/java/patterns/chainofresponsibility/README.md) for when *not* to use it.

The repo's [`problems/logger`](../../src/main/java/problems/logger) is the same design with an abstract base class doing
the forwarding.
