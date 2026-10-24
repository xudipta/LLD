package patterns.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Playlist implements Iterable<String> {
    private final List<String> songs = new ArrayList<>();

    public Playlist add(String song) {
        songs.add(song);
        return this;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private int next = 0;

            @Override
            public boolean hasNext() { return next < songs.size(); }

            @Override
            public String next() {
                if (!hasNext()) throw new NoSuchElementException();
                return songs.get(next++);
            }
        };
    }

    /** A second traversal strategy over the same collection. */
    public Iterable<String> reversed() {
        return () -> new Iterator<>() {
            private int next = songs.size() - 1;

            @Override
            public boolean hasNext() { return next >= 0; }

            @Override
            public String next() {
                if (!hasNext()) throw new NoSuchElementException();
                return songs.get(next--);
            }
        };
    }
}
