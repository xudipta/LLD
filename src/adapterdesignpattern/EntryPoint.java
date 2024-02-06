package adapterdesignpattern;

import adapterdesignpattern.adaptee.WeightForBabies;
import adapterdesignpattern.adaptee.WeightMachine;
import adapterdesignpattern.adapter.WeightMachineAdapter;
import adapterdesignpattern.adapter.WeightMachineAdapterImpl;

public class EntryPoint {

    public static void main(String[] args) {
        WeightMachine weightMachine = new WeightForBabies();
        WeightMachineAdapter adapter = new WeightMachineAdapterImpl(weightMachine);

        System.out.println("Weight in pound: "+weightMachine.getWeightInPounds());

        System.out.println("Weight in kd: "+adapter.getWeightInKg());

    }
}
