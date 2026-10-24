package strategydesignpattern;

import strategydesignpattern.strategies.NormalDriveStrategy;

public class PassengerVehicle extends Vehicle{
    PassengerVehicle() {
        super(new NormalDriveStrategy());
    }
}
