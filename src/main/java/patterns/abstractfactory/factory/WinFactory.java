package patterns.abstractfactory.factory;

import patterns.abstractfactory.product.Button;
import patterns.abstractfactory.product.Checkbox;
import patterns.abstractfactory.product.WinButton;
import patterns.abstractfactory.product.WinCheckbox;

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
