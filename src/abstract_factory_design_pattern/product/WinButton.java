package abstract_factory_design_pattern.product;

public class WinButton implements Button{
    @Override
    public void paint() {
        System.out.println("Windows button painted");
    }
}
