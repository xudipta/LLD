package abstract_factory_design_pattern.product;

public class MacButton implements Button{
    @Override
    public void paint() {
        System.out.println("MAC button painted");
    }
}
