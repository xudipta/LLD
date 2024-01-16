package strategy_design_pattern.strategies;

public class NormalDriveStrategy implements DriveStrategy{
    @Override
    public void drive() {
        System.out.println("Drive in normal mode");
    }
}
