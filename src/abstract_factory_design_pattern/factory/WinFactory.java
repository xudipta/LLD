package abstract_factory_design_pattern.factory;

import abstract_factory_design_pattern.product.Button;
import abstract_factory_design_pattern.product.Checkbox;
import abstract_factory_design_pattern.product.WinButton;
import abstract_factory_design_pattern.product.WinCheckbox;

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
