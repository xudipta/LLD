package strategy_design_pattern;

import strategy_design_pattern.strategies.DriveStrategy;
import strategy_design_pattern.strategies.NormalDriveStrategy;

public class PassengerVehicle extends Vehicle{
    PassengerVehicle() {
        super(new NormalDriveStrategy());
    }
}
