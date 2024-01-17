package abstract_factory_design_pattern.factory;

import abstract_factory_design_pattern.product.Button;
import abstract_factory_design_pattern.product.Checkbox;
import abstract_factory_design_pattern.product.MacButton;
import abstract_factory_design_pattern.product.MacCheckbox;

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
