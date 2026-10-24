# Builder

> Construct a complex immutable object step by step, validating it once at the end.

## 1. Problem
An HTTP request has a required URL and several optional parts. A constructor with six parameters is unreadable, and setters allow half-built objects.

## 2. Class diagram
```mermaid
classDiagram
    class HttpRequest {
        -method
        -url
        -headers
        -body
        +builder()$ Builder
    }
    class Builder {
        +method(m) Builder
        +url(u) Builder
        +header(k, v) Builder
        +body(b) Builder
        +build() HttpRequest
    }
    HttpRequest +-- Builder
    Builder ..> HttpRequest : creates
```

## 3. How it works
Callers chain setter-style calls on a `Builder` and finish with `build()`, which validates (URL required, no body on GET) and returns an immutable `HttpRequest` with a read-only header map.

## 4. Design decisions
| Decision | Why |
|---|---|
| Private `HttpRequest` constructor | Objects can only be created through the builder, so they are always valid. |
| Validation in `build()` | Cross-field rules (method vs body) can only be checked once everything is set. |
| Header map copied and wrapped read-only | The built object stays immutable even if the builder is reused. |

## 5. Trade-offs and alternatives rejected
- **Telescoping constructors**: break down past three or four parameters.
- **JavaBean setters**: allow invalid intermediate states and make immutability impossible.
- **Records / static factories**: great when there are few fields and no cross-field rules.

**When NOT to use it:** the object has two or three fields, or all are required. A constructor or a record is shorter.

## 6. SOLID notes
**S** (construction rules live in one place), **O** (new optional fields don't break callers).

## 7. Known limitations
- Builder duplicates every field; Lombok or records reduce the boilerplate.
- Reusing one builder for two requests shares its header map until `build()` copies it.

## 8. Interview follow-ups
- Make `url` required at compile time (step builder)?
- Builder with a director for common presets?
- Why not a record here?

## 9. Related topics
[Prototype](../prototype/README.md), [Factory](../factory/README.md)

## Run it
```
mvn -q exec:java -Dexec.mainClass=patterns.builder.EntryPoint
mvn -q test -Dtest='patterns/builder/*'
```
