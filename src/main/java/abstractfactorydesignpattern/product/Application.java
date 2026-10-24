package abstractfactorydesignpattern.product;

import abstractfactorydesignpattern.factory.GUIFactory;

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
