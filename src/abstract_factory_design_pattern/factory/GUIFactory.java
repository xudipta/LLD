package abstract_factory_design_pattern.factory;

import abstract_factory_design_pattern.product.Button;
import abstract_factory_design_pattern.product.Checkbox;

public interface GUIFactory {
    Button createButton();
    Checkbox createCheckBox();
}
