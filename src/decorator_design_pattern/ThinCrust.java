package decorator_design_pattern;

public class ThinCrust extends PizzaBase{

    @Override
    public int getCost() {
        return 200;
    }

    @Override
    public String getDetails() {
        return "Thin-Crust ";
    }
}
