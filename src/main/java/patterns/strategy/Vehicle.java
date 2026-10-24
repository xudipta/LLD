package patterns.strategy;

import patterns.strategy.strategies.DriveStrategy;

public class Vehicle {
    DriveStrategy obj;
    Vehicle(DriveStrategy obj){
        this.obj=obj;
    }
    public void drive(){
        obj.drive();
    }


}
