package patterns.composite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectoryTest {
    @Test
    void emptyDirectoryHasSizeZero() {
        assertEquals(0, new Directory("d").getSize());
    }

    @Test
    void sizeIsTheSumOfChildren() {
        Directory d = new Directory("d").add(new File("a", 10)).add(new File("b", 5));
        assertEquals(15, d.getSize());
    }

    @Test
    void nestedDirectoriesAreIncluded() {
        Directory root = new Directory("root")
                .add(new File("a", 1))
                .add(new Directory("sub").add(new File("b", 2)).add(new Directory("deep").add(new File("c", 3))));
        assertEquals(6, root.getSize());
    }
}
