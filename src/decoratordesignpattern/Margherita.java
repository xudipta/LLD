package decoratordesignpattern;

public class Margherita extends PizzaBase{
    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public String getDetails() {
        return "Margherita ";
    }
}
