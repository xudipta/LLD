# Chain of Responsibility

> Pass a request along a chain of handlers until one of them deals with it.

The worked example for this pattern is the **[Logger](../../problems/logger/README.md)**: `Info -> Debug -> Error` processors, each handling its own level and forwarding the rest. Code and tests live in `problems.logger`; this page exists so the pattern is findable from the pattern list.

## When to reach for it
- Several objects may handle a request and the handler isn't known up front.
- Handlers should be added, removed or reordered without touching the sender.
- Typical uses: logging levels, servlet/HTTP middleware, approval workflows, input validation pipelines.

**When NOT to use it:** each request has exactly one known handler (use a map or a `switch`), or a request must always be handled (a chain can silently drop it, as the logger does for an unknown level).

## Related topics
[Logger](../../problems/logger/README.md), [Command](../command/README.md), [Decorator](../decorator/README.md)
