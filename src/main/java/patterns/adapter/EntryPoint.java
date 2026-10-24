package patterns.adapter;

import patterns.adapter.adaptee.WeightForBabies;
import patterns.adapter.adaptee.WeightMachine;
import patterns.adapter.adapter.WeightMachineAdapter;
import patterns.adapter.adapter.WeightMachineAdapterImpl;

public class EntryPoint {

    public static void main(String[] args) {
        WeightMachine weightMachine = new WeightForBabies();
        WeightMachineAdapter adapter = new WeightMachineAdapterImpl(weightMachine);

        System.out.println("Weight in pound: "+weightMachine.getWeightInPounds());

        System.out.println("Weight in kd: "+adapter.getWeightInKg());

    }
}
