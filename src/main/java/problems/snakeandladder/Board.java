package problems.snakeandladder;

import java.util.HashMap;
import java.util.Map;

/** Squares are numbered 1..size. Snakes and ladders are both "jumps" from one square to another. */
public class Board {
    private final int size;
    private final Map<Integer, Integer> jumps = new HashMap<>();

    public Board(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("size must be at least 2");
        }
        this.size = size;
    }

    public Board addLadder(int bottom, int top) {
        if (top <= bottom) throw new IllegalArgumentException("A ladder must go up");
        return addJump(bottom, top);
    }

    public Board addSnake(int head, int tail) {
        if (tail >= head) throw new IllegalArgumentException("A snake must go down");
        return addJump(head, tail);
    }

    private Board addJump(int from, int to) {
        if (from < 2 || from >= size || to < 1 || to > size) {
            throw new IllegalArgumentException("Jump out of range: " + from + " -> " + to);
        }
        if (jumps.containsKey(from) || jumps.containsValue(from) || jumps.containsKey(to)) {
            throw new IllegalArgumentException("Jumps may not chain or share a square: " + from + " -> " + to);
        }
        jumps.put(from, to);
        return this;
    }

    /** The square a token ends on after landing on {@code square}. */
    public int resolve(int square) {
        return jumps.getOrDefault(square, square);
    }

    public int getSize() { return size; }
}
