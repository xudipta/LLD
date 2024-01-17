package abstract_factory_design_pattern.product;

public class MacCheckbox implements Checkbox{
    @Override
    public void paint() {
        System.out.println("MAC checkbox painted");
    }
}
