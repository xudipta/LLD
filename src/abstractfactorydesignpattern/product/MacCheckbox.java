package abstractfactorydesignpattern.product;

public class MacCheckbox implements Checkbox{
    @Override
    public void paint() {
        System.out.println("MAC checkbox painted");
    }
}
