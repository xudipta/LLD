package patterns.abstractfactory.factory;

import patterns.abstractfactory.product.Button;
import patterns.abstractfactory.product.Checkbox;

public interface GUIFactory {
    Button createButton();
    Checkbox createCheckBox();
}
