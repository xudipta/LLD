package strategydesignpattern.strategies;

public class NormalDriveStrategy implements DriveStrategy{
    @Override
    public void drive() {
        System.out.println("Drive in normal mode");
    }
}
