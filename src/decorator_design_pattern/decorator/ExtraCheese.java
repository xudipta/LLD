package decorator_design_pattern.decorator;

import decorator_design_pattern.PizzaBase;

public class ExtraCheese extends ToppingsDecorator{
    PizzaBase base;

    public ExtraCheese(PizzaBase base){
        this.base = base;
    }

    @Override
    public int getCost() {
        return base.getCost() + 60;
    }

    @Override
    public String getDetails() {
        return base.getDetails()+"Extra-Cheese ";
    }
}
