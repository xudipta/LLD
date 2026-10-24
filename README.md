# LLD

[![CI](https://github.com/xudipta/LLD/actions/workflows/ci.yml/badge.svg)](https://github.com/xudipta/LLD/actions/workflows/ci.yml)

A Java handbook for **low-level design (LLD)**: classic design patterns and interview-style
problems, each with runnable code, tests, a class diagram and the trade-offs behind the design.

It is for people preparing for LLD interviews, and for anyone who wants to see *why* a pattern
is used rather than only what it looks like.

## Contents

### Design patterns
| Pattern | Example | Docs |
|---|---|---|
| Strategy | Vehicle drive modes | [patterns/strategy](src/main/java/patterns/strategy/README.md) |
| Observer | Back-in-stock notifications | [patterns/observer](src/main/java/patterns/observer/README.md) |
| Decorator | Pizza toppings | [patterns/decorator](src/main/java/patterns/decorator/README.md) |
| Factory | Shapes | [patterns/factory](src/main/java/patterns/factory/README.md) |
| Abstract Factory | Cross-platform GUI | [patterns/abstractfactory](src/main/java/patterns/abstractfactory/README.md) |
| Adapter | Pounds to kilograms | [patterns/adapter](src/main/java/patterns/adapter/README.md) |

### Design problems
| Problem | Patterns used | Docs |
|---|---|---|
| Tic-Tac-Toe | Composition, queue-based turns | [problems/tictactoe](src/main/java/problems/tictactoe/README.md) |
| Logger | Chain of Responsibility | [problems/logger](src/main/java/problems/logger/README.md) |

## Suggested learning path
1. Start with [Strategy](src/main/java/patterns/strategy/README.md) and [Factory](src/main/java/patterns/factory/README.md).
2. Then [Observer](src/main/java/patterns/observer/README.md), [Decorator](src/main/java/patterns/decorator/README.md) and [Adapter](src/main/java/patterns/adapter/README.md).
3. Finish with [Abstract Factory](src/main/java/patterns/abstractfactory/README.md).
4. Apply them in [Tic-Tac-Toe](src/main/java/problems/tictactoe/README.md), then the [Logger](src/main/java/problems/logger/README.md).

## Build, run and test
Requires JDK 21 and Maven.

```bash
mvn verify                                   # compile and run all tests
mvn -q exec:java -Dexec.mainClass=patterns.strategy.EntryPoint
mvn -q exec:java -Dexec.mainClass=problems.tictactoe.EntryPoint
```

Every topic has an `EntryPoint` class with a `main` method.

## Project layout
```
src/main/java/patterns/<name>/    one folder per design pattern
src/main/java/problems/<name>/    one folder per design problem
src/main/java/basics/             small Java language demos
src/test/java/                    JUnit 5 tests, mirroring the main packages
docs/TEMPLATE.md                  structure every topic README follows
```

## Roadmap
Planned work is tracked in the [issues](https://github.com/xudipta/LLD/issues): more patterns,
more interview problems (parking lot, elevator, rate limiter and others), concurrency-aware
designs, "naive to refactored" walkthroughs, a pattern-selection guide and an interview framework.

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md).
