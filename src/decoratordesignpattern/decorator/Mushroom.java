package decoratordesignpattern.decorator;

import decoratordesignpattern.PizzaBase;

public class Mushroom extends ToppingsDecorator{
    PizzaBase base;

    public Mushroom(PizzaBase base){
        this.base=base;
    }
    @Override
    public int getCost() {
        return base.getCost()+50;
    }

    @Override
    public String getDetails() {
        return base.getDetails()+"Mushroom ";
    }
}
