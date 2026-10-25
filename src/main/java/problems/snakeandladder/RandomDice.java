package problems.snakeandladder;

import java.util.Random;

public class RandomDice implements Dice {
    private final int sides;
    private final Random random;

    public RandomDice(int sides, Random random) {
        this.sides = sides;
        this.random = random;
    }

    @Override
    public int roll() {
        return random.nextInt(sides) + 1;
    }
}
