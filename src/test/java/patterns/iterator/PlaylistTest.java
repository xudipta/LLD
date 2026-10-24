package patterns.iterator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    private final Playlist playlist = new Playlist().add("A").add("B").add("C");

    private List<String> collect(Iterable<String> it) {
        List<String> out = new ArrayList<>();
        it.forEach(out::add);
        return out;
    }

    @Test
    void iteratesInInsertionOrder() {
        assertEquals(List.of("A", "B", "C"), collect(playlist));
    }

    @Test
    void iteratesInReverse() {
        assertEquals(List.of("C", "B", "A"), collect(playlist.reversed()));
    }

    @Test
    void emptyPlaylistHasNoElements() {
        assertFalse(new Playlist().iterator().hasNext());
    }

    @Test
    void nextPastTheEndThrows() {
        Iterator<String> it = new Playlist().add("A").iterator();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorsAreIndependent() {
        Iterator<String> first = playlist.iterator();
        first.next();
        assertEquals("A", playlist.iterator().next());
    }
}
