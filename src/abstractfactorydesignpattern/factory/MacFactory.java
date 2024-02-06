package abstractfactorydesignpattern.factory;

import abstractfactorydesignpattern.product.Button;
import abstractfactorydesignpattern.product.Checkbox;
import abstractfactorydesignpattern.product.MacButton;
import abstractfactorydesignpattern.product.MacCheckbox;

public class MacFactory implements GUIFactory{
    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public Checkbox createCheckBox() {
        return new MacCheckbox();
    }
}
