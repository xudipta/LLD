# Tic-Tac-Toe: from one method to a clean design

Final design and trade-offs: [problems/tictactoe](../../src/main/java/problems/tictactoe/README.md).
Format of this walkthrough: [README](README.md).

All four steps pass the same nine scenarios in
[`BehaviourTest`](../../src/test/java/walkthroughs/tictactoe/BehaviourTest.java): a win on a row, a column, a
diagonal and the anti-diagonal; a draw; an occupied square; squares off the board; and "an invalid move does not cost
the player their turn".

---

## Step 1: it works
[`step1/TicTacToe.java`](../../src/main/java/walkthroughs/tictactoe/step1/TicTacToe.java)

One class, one method. `move(r, c)` validates, places a `char`, **prints the board**, checks eight hard-coded winning
lines and flips `turn`. It returns `"X"`, `"O"`, `"DRAW"`, `"INVALID"` or `"CONTINUE"` as a `String`.

### Smells
1. **Eight copy-pasted win conditions.** One typo in one of them is a silent bug, and each line is the same idea.
2. **The size is baked in.** `3`, `2` and `9` are scattered through the method; a 4x4 board means rewriting it.
3. **Rules and output are mixed.** `move` prints, so you cannot use it from a test or a web app without console noise.
4. **Stringly typed result.** `"DRAW"` vs `"Draw"` compiles fine and fails at runtime.
5. **Rescans the whole board** after every move to see whether the *current* player has won.

### Refactoring
*Extract class* (`Board`) and *replace magic numbers with a parameter*; replace the eight conditions with loops; replace
`String` results with an `enum`; make rendering return text instead of printing.

---

## Step 2: a Board, a size, an enum
[`step2/`](../../src/main/java/walkthroughs/tictactoe/step2)

`Board` owns the grid and its placement rules (`place` returns whether the move was legal). `TicTacToe.hasWon` loops over
rows, columns and both diagonals for any size. `Outcome` is an enum. `Board.render()` returns a `String`.

**New in this step:** a 4x4 game works ([`LaterStepsTest`](../../src/test/java/walkthroughs/tictactoe/LaterStepsTest.java)).

### Smells that remain
1. **Players are a `char`** and `turn == 'X' ? 'O' : 'X'` hard-codes exactly two of them.
2. **`X_WINS` / `O_WINS` are outcomes baked into the enum.** A third player or a different symbol means a new constant.
3. **The win check still scans every row and column** on every move, not just the ones through the square just played.

### Refactoring
*Introduce class* (`Player`), *replace flag with a queue* for turn order, and *narrow the check* to the last move.

---

## Step 3: players, a turn queue, a smarter win check
[`step3/`](../../src/main/java/walkthroughs/tictactoe/step3)

`Player(name, symbol)` is a record. Turn order is a `Deque<Player>`: after a legal move the front player goes to the back,
after an invalid move nothing changes. `MoveResult(outcome, winner)` says who won instead of having `X_WINS` constants.
`winsThrough(r, c, p)` checks only the row, column and (if the square is on one) the diagonals through the square just
played: O(n) per move.

### Smells that remain
1. **`playInteractively()` lives inside the game class.** Prompts, `Scanner` parsing and rules are in one place, so the
   input handling cannot be tested and `Integer.parseInt` crashes on `"abc"` or a lone number.
2. The game cannot be run from anything but the real `System.in`.

### Refactoring
*Extract class* (`ConsoleRunner`) and *inject dependencies* (input and output streams).

---

## Step 4: rules separated from I/O
[`step4/`](../../src/main/java/walkthroughs/tictactoe/step4)

`TicTacToe` is pure rules: no `Scanner`, no `println`. `ConsoleRunner` takes an `InputStream` and a `PrintStream`,
accepts `1 2` or `1,2`, and re-prompts on garbage instead of crashing.

**New in this step:** the console loop is tested with scripted input: a whole game, garbage input and illegal moves, and a
tie ([`LaterStepsTest`](../../src/test/java/walkthroughs/tictactoe/LaterStepsTest.java)). Three players also work.

### What a reader should take away
- Every step kept the same nine behaviours green. If one of those scenarios had broken, the refactoring would have been wrong.
- Each step fixed **the most expensive smell**, not every smell at once.
- "Testable" was a design goal that drove the changes: injecting the clock/streams, returning data instead of printing.

### What is still not perfect
- The repo's own [`problems/tictactoe`](../../src/main/java/problems/tictactoe) is a variant of step 3 and still has the
  interactive loop inside the game class, and its "play again" does not reset the board (listed in its README). Step 4 is
  the shape it should be refactored towards.
