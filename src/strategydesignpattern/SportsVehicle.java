package strategydesignpattern;
import strategydesignpattern.strategies.SportsDriveStrategy;
public class SportsVehicle extends Vehicle{
    SportsVehicle() {
        super(new SportsDriveStrategy());
    }
}
