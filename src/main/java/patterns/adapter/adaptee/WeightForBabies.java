package patterns.adapter.adaptee;

public class WeightForBabies implements WeightMachine{
    @Override
    public double getWeightInPounds() {
        return 30;
    }
}
