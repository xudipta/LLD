package patterns.prototype;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {
    @Test
    void copyIsEqualInContentButNotTheSameObject() {
        Document original = new Document("T", List.of("p1"));
        Document copy = original.copy();
        assertNotSame(original, copy);
        assertEquals(original.getPages(), copy.getPages());
    }

    @Test
    void changingTheCopyDoesNotAffectTheOriginal() {
        Document original = new Document("T", List.of("p1"));
        Document copy = original.copy();
        copy.setTitle("Other");
        copy.getPages().add("p2");
        assertEquals("T", original.getTitle());
        assertEquals(List.of("p1"), original.getPages());
    }
}
