# Pattern selection guide

Start from the problem you have, not the pattern you know. Every pattern name below
links to a worked example in this repo.

## Which pattern do I need?

### Creating objects
| I have this problem | Use | Not this |
|---|---|---|
| Callers shouldn't know which concrete class they get | [Factory](../src/main/java/patterns/factory/README.md) | `new` scattered everywhere |
| Objects must come in matching families (Mac widgets with Mac widgets) | [Abstract Factory](../src/main/java/patterns/abstractfactory/README.md) | Plain Factory, which can't keep a family consistent |
| An object has many optional parts or cross-field rules | [Builder](../src/main/java/patterns/builder/README.md) | A 7-argument constructor or setters |
| I need cheap copies of a configured object | [Prototype](../src/main/java/patterns/prototype/README.md) | `Cloneable` |
| There must be exactly one instance | [Singleton](../src/main/java/patterns/singleton/README.md) | A mutable global; prefer dependency injection |

### Structuring objects
| I have this problem | Use | Not this |
|---|---|---|
| An existing class has the wrong interface | [Adapter](../src/main/java/patterns/adapter/README.md) | Editing code you don't own |
| I want to add behaviour at runtime and combine additions | [Decorator](../src/main/java/patterns/decorator/README.md) | A subclass per combination |
| I want to control access (cache, lazy load, permissions) | [Proxy](../src/main/java/patterns/proxy/README.md) | A Decorator, whose purpose is adding behaviour |
| A subsystem is hard to use correctly | [Facade](../src/main/java/patterns/facade/README.md) | Documenting a 5-step ritual |
| Items and groups of items must be treated the same | [Composite](../src/main/java/patterns/composite/README.md) | `instanceof` checks |

### Behaviour
| I have this problem | Use | Not this |
|---|---|---|
| One behaviour has interchangeable algorithms, chosen at runtime | [Strategy](../src/main/java/patterns/strategy/README.md) | A growing `if/else` or subclass-per-variant |
| The algorithm's skeleton is fixed, only steps vary | [Template Method](../src/main/java/patterns/templatemethod/README.md) | Copy-pasted near-identical methods |
| Many objects must react when one changes | [Observer](../src/main/java/patterns/observer/README.md) | The subject calling each dependent by name |
| Behaviour depends on a state with legal transitions | [State](../src/main/java/patterns/state/README.md) | Status flags checked in every method |
| I need undo, queuing or logging of actions | [Command](../src/main/java/patterns/command/README.md) | Direct method calls |
| A request may be handled by one of several handlers in order | [Chain of Responsibility](../src/main/java/patterns/chainofresponsibility/README.md) | One method that knows every handler |
| I need to traverse a structure without exposing it | [Iterator](../src/main/java/patterns/iterator/README.md) | Returning the internal list |

## Easily confused pairs
| Pair | The difference |
|---|---|
| Strategy vs State | Both delegate to an object. A strategy is picked by the *client* and rarely changes; a state changes *itself* as the object moves through its lifecycle. |
| Decorator vs Proxy | Same shape. Decorator *adds* behaviour; Proxy *controls access* to the same behaviour. |
| Adapter vs Facade | Adapter makes one interface fit another; Facade makes many classes simpler to use. |
| Factory vs Abstract Factory | Factory returns one kind of product; Abstract Factory returns a consistent *family*. |
| Template Method vs Strategy | Template uses inheritance (fixed at compile time); Strategy uses composition (swappable at runtime). |
| Builder vs Prototype | Builder assembles a new object from parts; Prototype clones an existing one. |

## Common misuse
- **Singleton as a global variable.** It hides dependencies and makes tests share state.
- **Factory for one class.** If there is only one implementation and none is planned, `new` is fine.
- **Observer with side effects that depend on order.** Observers should be independent.
- **State machines as inheritance trees for two states.** An enum and a switch is clearer.
- **Decorator stacks that need to be unwrapped.** If you must inspect or remove layers, use a list.
- **Patterns first, requirements second.** Design for the requirements you have, then name the pattern you ended up with.

## Which patterns do the problems use?
| Problem | Patterns | Where it shows |
|---|---|---|
| [Tic-Tac-Toe](../src/main/java/problems/tictactoe/README.md) | Composition; queue-based turn rotation; a small class hierarchy for pieces | `Deque<Player>`, `Board`, `PlayingPiece` |
| [Logger](../src/main/java/problems/logger/README.md) | Chain of Responsibility | `LogProcessor` handlers |
| [Parking lot](../src/main/java/problems/parkinglot/README.md) | Strategy (twice) | `SpotAllocationStrategy`, `PricingStrategy` |
| [LRU cache](../src/main/java/problems/lrucache/README.md) | Decorator-style wrapping for thread safety | `ConcurrentLruCache` around `LruCore` |
| [Rate limiter](../src/main/java/problems/ratelimiter/README.md) | Strategy-style interface | `RateLimiter` with swappable algorithms |
| [Vending machine](../src/main/java/problems/vendingmachine/README.md) | State | `IdleState`, `HasMoneyState` |
| [Elevator system](../src/main/java/problems/elevator/README.md) | Strategy | `DispatchStrategy` picks the car |
| [Snake and Ladder](../src/main/java/problems/snakeandladder/README.md) | Dependency injection of a collaborator | `Dice` interface, scripted in tests |

Each new problem added to the repo should add a row here, and its README should link back
to the pattern pages it uses.
