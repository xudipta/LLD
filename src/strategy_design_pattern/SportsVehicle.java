package strategy_design_pattern;
import strategy_design_pattern.strategies.SportsDriveStrategy;
public class SportsVehicle extends Vehicle{
    SportsVehicle() {
        super(new SportsDriveStrategy());
    }
}
