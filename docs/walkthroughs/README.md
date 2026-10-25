# Walkthroughs: from naive to well designed

Most design resources show the finished answer. These show **how you get there**: start with
code that works but smells, name the smells, and apply one refactoring at a time.

Three topics have a walkthrough:

| Topic | Steps | Doc | Code |
|---|---|---|---|
| Tic-Tac-Toe | 4 | [tictactoe.md](tictactoe.md) | `src/main/java/walkthroughs/tictactoe/step1` to `step4` |
| Logger | 3 | [logger.md](logger.md) | `src/main/java/walkthroughs/logger/step1` to `step3` |
| Parking lot | 2, then the final design | [parkinglot.md](parkinglot.md) | `src/main/java/walkthroughs/parkinglot/step1`, `step2`, then `problems.parkinglot` |

## The format
Every walkthrough follows the same shape:

1. **Step N** - what the code does now, with the key lines.
2. **Smells** - what is wrong, in plain words, and what each would cost you later.
3. **Refactoring** - the one change that fixes the most important smell, named (extract class, replace conditional with strategy, ...).
4. **What did not change** - the behaviour that stays identical.

Rules of the format:
- **Every step is real, compiling code** in its own package, not a snippet.
- **One behaviour test runs against every step.** Each topic has a `BehaviourTest` that plays the same scenarios against each step through a small adapter. The code changes a lot between steps; the observable behaviour must not. That test is what makes the refactoring safe, and it is the point of the exercise.
- **Some tests only exist from a later step on.** If a step makes something testable for the first time (injecting a clock, separating I/O), the test is added at that step, and the doc says so.
- **Each step fixes the biggest smell, not all of them.** Real refactoring happens in small safe moves.

## How to use them
- Read a step, then try to list the smells yourself before reading the doc's list.
- Run the behaviour test: `mvn -q test -Dtest=BehaviourTest`. Change something in one step and watch which scenario fails.
- To see the diff between steps: `diff -r src/main/java/walkthroughs/tictactoe/step2 src/main/java/walkthroughs/tictactoe/step3`.

## Adding a walkthrough
Follow the format above: a `stepN` package per stage, a `BehaviourTest` with an adapter per step, and a doc in this folder. Keep steps small enough that the smell list for each fits on one screen.
