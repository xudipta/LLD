package implementations.lambda;

public class EntryPoint {
    public static void main(String[] args) {
        printThing(()->{
            System.out.println("Printing using lambda expression");
        });
    }
    static void printThing(Printable printable){
        printable.print();
    }
}
