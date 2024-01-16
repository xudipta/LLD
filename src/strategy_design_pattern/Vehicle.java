package strategy_design_pattern;

import strategy_design_pattern.strategies.DriveStrategy;

public class Vehicle {
    DriveStrategy obj;
    Vehicle(DriveStrategy obj){
        this.obj=obj;
    }
    public void drive(){
        obj.drive();
    }


}
