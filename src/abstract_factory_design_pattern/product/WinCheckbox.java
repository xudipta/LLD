package abstract_factory_design_pattern.product;

public class WinCheckbox implements Checkbox{
    @Override
    public void paint() {
        System.out.println("Windows checkbox painted");
    }
}
