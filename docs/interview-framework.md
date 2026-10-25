# A 45-minute framework for low-level design interviews

The same order works for every problem in this repo, and every problem README follows it
(see the mapping at the bottom). The time boxes are guides, not rules.

| Step | Minutes | Output |
|---|---|---|
| 1. Clarify requirements | 0-5 | A short list of must-haves and an explicit "out of scope" |
| 2. Find the entities | 5-10 | Nouns that become classes, with their responsibilities |
| 3. Define the APIs | 10-15 | Public method signatures for the main use cases |
| 4. Choose patterns | 15-20 | Where behaviour varies and what absorbs the variation |
| 5. Write the code | 20-38 | Core classes and the main flow, compiling in your head |
| 6. Extend and stress | 38-45 | Answers to "what if...", concurrency and testing |

## 1. Clarify requirements (0-5)
Never start designing before you've asked. Good questions are about **scope**, **scale** and **rules**.

Checklist:
- Who are the actors? (player, admin, customer)
- What are the 3 to 5 core use cases? Say them back to the interviewer.
- What are the rules and edge cases? (ties, invalid input, limits, duplicates)
- Is it single process or distributed? Is concurrency expected?
- What is explicitly **out of scope**? (persistence, UI, payments)
- Any extension already in mind? (bigger board, new vehicle types)

Write the agreed list down. You will check your design against it at the end.

## 2. Find the entities (5-10)
Underline the nouns in the requirements. For each, decide if it is:
- **a class with behaviour** (Board, ParkingLot),
- **a value** (Point, Money), or
- **an enum** (PlayingType, Level).

Then draw the relationships: *has-a* (composition) before *is-a* (inheritance). If you
can't say what a class is responsible for in one sentence, it is probably two classes
or no class.

## 3. Define the APIs (10-15)
Write the method signatures the main use cases need, before any bodies. For example:

```java
boolean addPiece(int r, int c, PlayingPiece piece);
boolean isWinner(int row, int col, Player p);
```

This forces decisions about who owns what, and makes the interviewer's questions
concrete. Return values should say what failure looks like (boolean, exception, Optional).

## 4. Choose patterns (15-20)
Ask "what will change?" and put an interface there. Use the
[pattern guide](pattern-guide.md): find the row that matches your problem.
Name the pattern only after you've described the problem it solves.

Rules of thumb:
- Behaviour that varies at runtime -> Strategy or State.
- Creation that varies -> Factory.
- Many things reacting to one event -> Observer.
- If nothing varies, don't add a pattern.

## 5. Write the code (20-38)
Order matters:
1. Models and enums (fast, confirms the vocabulary).
2. The core interface and one implementation.
3. The main flow end to end for one happy-path use case.
4. Validation and edge cases.
5. Skip getters, setters and main() unless asked.

Narrate trade-offs as you go: "I'm using a Deque so turn order and retry are one-liners."

## 6. Extend and stress (38-45)
Prepare answers for these every time:
- **Extend:** a new type/rule/variant: which classes change? Ideally none of the existing ones.
- **Scale up:** bigger input, more users.
- **Concurrency:** what is shared and mutable? Where is the lock, or which concurrent collection?
- **Testing:** what are the seams? Pure logic (board, win check) is easy to test; console I/O isn't.
- **Failure:** invalid input, partial failure, what the user sees.

## Worked example: Tic-Tac-Toe
| Step | What you'd say or produce |
|---|---|
| Clarify | Two players, N x N board (3 by default), X and O alternate, first full row/column/diagonal wins, full board with no winner is a tie. Illegal move: same player retries. Out of scope: AI, undo, networking. |
| Entities | `Board` (grid and placement rule), `Player` (name, piece), `PlayingPiece` (X or O), `TicTacToeGame` (turns and winning). |
| APIs | `Board.addPiece(r, c, piece)` returns whether the move was legal; `TicTacToeGame.startGame()` returns the winner or "tie"; `isWinner(row, col, player)`. |
| Patterns | None needed for the core. Pieces are a small hierarchy so a new symbol is a subclass. A `Deque<Player>` handles turn rotation. |
| Code | Models first, then `Board.addPiece`, then the turn loop, then the win check scanning only the row, column and diagonals through the last move. |
| Extend | Bigger board: already a constructor argument. AI: extract a move source interface (Strategy). Undo: keep a move stack (Command). |
| Stress | Win check is O(N) per move; per-line counters make it O(1). Console I/O is separate from rules so the rules are unit-tested. |

Full write-up: [problems/tictactoe](../src/main/java/problems/tictactoe/README.md).

## Common mistakes
- Coding before clarifying.
- Reaching for a pattern before there is a problem for it.
- Inheritance where composition would do.
- Forgetting invalid input and concurrency.
- Going silent. Say what you're weighing.

## How the problem READMEs follow this order
| Framework step | README section |
|---|---|
| 1. Clarify | 1. Problem |
| 2. Entities | 2. Class diagram |
| 3. APIs | 3. How it works |
| 4. Patterns | 4. Design decisions, 5. Trade-offs |
| 5. Code | the source in the same folder, and 6. SOLID notes |
| 6. Extend and stress | 7. Known limitations, 8. Interview follow-ups |

The template is in [TEMPLATE.md](TEMPLATE.md).
