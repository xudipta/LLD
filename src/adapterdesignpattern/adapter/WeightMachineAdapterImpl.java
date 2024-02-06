package adapterdesignpattern.adapter;

import adapterdesignpattern.adaptee.WeightMachine;

public class WeightMachineAdapterImpl implements WeightMachineAdapter{
    WeightMachine weightMachine;
    public WeightMachineAdapterImpl(WeightMachine weightMachine){
        this.weightMachine=weightMachine;
    }
    @Override
    public double getWeightInKg() {
        return weightMachine.getWeightInPounds()*.45;
    }
}
