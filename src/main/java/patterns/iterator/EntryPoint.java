package patterns.iterator;

public class EntryPoint {
    public static void main(String[] args) {
        Playlist playlist = new Playlist().add("One").add("Two").add("Three");
        for (String song : playlist) System.out.println(song);
        System.out.println("--- reversed");
        for (String song : playlist.reversed()) System.out.println(song);
    }
}
