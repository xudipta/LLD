package patterns.prototype;

import java.util.ArrayList;
import java.util.List;

public class Document implements Prototype<Document> {
    private String title;
    private final List<String> pages;

    public Document(String title, List<String> pages) {
        this.title = title;
        this.pages = new ArrayList<>(pages);
    }

    /** Deep copy: the mutable page list is duplicated, not shared. */
    @Override
    public Document copy() {
        return new Document(title, pages);
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<String> getPages() { return pages; }
}
