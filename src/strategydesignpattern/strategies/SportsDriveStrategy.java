package strategydesignpattern.strategies;

public class SportsDriveStrategy implements DriveStrategy{
    @Override
    public void drive() {
        System.out.println("Drive in sports mode");
    }
}
