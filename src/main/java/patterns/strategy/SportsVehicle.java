package patterns.strategy;
import patterns.strategy.strategies.SportsDriveStrategy;
public class SportsVehicle extends Vehicle{
    SportsVehicle() {
        super(new SportsDriveStrategy());
    }
}
