package patterns.facade;

public class EntryPoint {
    public static void main(String[] args) {
        HomeTheaterFacade theater = new HomeTheaterFacade();
        theater.watchMovie().forEach(System.out::println);
        theater.endMovie().forEach(System.out::println);
    }
}
