package patterns.abstractfactory.factory;

import patterns.abstractfactory.product.Button;
import patterns.abstractfactory.product.Checkbox;
import patterns.abstractfactory.product.MacButton;
import patterns.abstractfactory.product.MacCheckbox;

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
