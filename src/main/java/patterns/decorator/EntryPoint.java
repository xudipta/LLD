package patterns.decorator;

import patterns.decorator.decorator.ExtraCheese;
import patterns.decorator.decorator.Mushroom;

public class EntryPoint {
    public static void main(String[] args) {
        PizzaBase pizza1 = new ExtraCheese(new ThinCrust());
        System.out.println("cost is: "+pizza1.getDetails());
        System.out.println("cost is: "+pizza1.getCost());

        PizzaBase pizza2 = new Mushroom(pizza1);
        System.out.println("cost is: "+pizza2.getDetails());
        System.out.println("cost is: "+pizza2.getCost());
    }
}
