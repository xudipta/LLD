package strategy_design_pattern.strategies;

public class SportsDriveStrategy implements DriveStrategy{
    @Override
    public void drive() {
        System.out.println("Drive in sports mode");
    }
}
