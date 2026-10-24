package abstractfactorydesignpattern.product;

public class WinButton implements Button{
    @Override
    public void paint() {
        System.out.println("Windows button painted");
    }
}
