package abstractfactorydesignpattern.factory;

import abstractfactorydesignpattern.product.Button;
import abstractfactorydesignpattern.product.Checkbox;
import abstractfactorydesignpattern.product.WinButton;
import abstractfactorydesignpattern.product.WinCheckbox;

public class WinFactory implements GUIFactory{
    @Override
    public Button createButton() {
        return new WinButton();
    }

    @Override
    public Checkbox createCheckBox() {
        return new WinCheckbox();
    }
}
