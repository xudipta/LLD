package patterns.abstractfactory.product;

import patterns.abstractfactory.factory.GUIFactory;

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
