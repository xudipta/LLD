package strategydesignpattern;

public class Entrypoint {
    public static void main(String[] args){
        Vehicle vehicle= new PassengerVehicle();
        vehicle.drive();
        vehicle = new SportsVehicle();
        vehicle.drive();
    }

}
