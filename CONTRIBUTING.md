# Contributing

## Adding a pattern or problem
1. Put code in `src/main/java/patterns/<name>/` or `src/main/java/problems/<name>/`.
2. Add an `EntryPoint` class with a `main` method that demonstrates it.
3. Add JUnit 5 tests under the matching package in `src/test/java/`.
4. Write a `README.md` in the same folder following [docs/TEMPLATE.md](docs/TEMPLATE.md),
   including honest **Known limitations**.
5. Add a row to the tables in the root [README](README.md).

## Before opening a PR
```bash
mvn -B verify
```
CI runs the same command.

## Conventions
- Package names are lowercase with no `designpattern` suffix.
- One issue per PR; reference it with `Closes #N`.
- Keep READMEs factual: every claim about the code should be checkable in the source.
