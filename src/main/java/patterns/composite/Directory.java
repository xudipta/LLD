package patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class Directory implements FileSystemItem {
    private final String name;
    private final List<FileSystemItem> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public Directory add(FileSystemItem item) {
        children.add(item);
        return this;
    }

    @Override
    public String getName() { return name; }

    /** Callers treat a directory exactly like a file: size is the sum of its children. */
    @Override
    public long getSize() {
        return children.stream().mapToLong(FileSystemItem::getSize).sum();
    }
}
