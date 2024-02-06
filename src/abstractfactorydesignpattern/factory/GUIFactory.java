package abstractfactorydesignpattern.factory;

import abstractfactorydesignpattern.product.Button;
import abstractfactorydesignpattern.product.Checkbox;

public interface GUIFactory {
    Button createButton();
    Checkbox createCheckBox();
}
