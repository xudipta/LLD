package abstract_factory_design_pattern.product;

import abstract_factory_design_pattern.factory.GUIFactory;

public class Application {
    Button button;
    Checkbox checkbox;
    public Application(GUIFactory factory){
        this.button = factory.createButton();
        this.checkbox = factory.createCheckBox();
    }

    public void paint(){
        button.paint();
        checkbox.paint();
    }

}
