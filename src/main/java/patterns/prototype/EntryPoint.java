package patterns.prototype;

import java.util.List;

public class EntryPoint {
    public static void main(String[] args) {
        Document template = new Document("Invoice template", List.of("Header", "Items", "Footer"));
        Document invoice = template.copy();
        invoice.setTitle("Invoice #1");
        invoice.getPages().add("Notes");
        System.out.println(template.getTitle() + " " + template.getPages());
        System.out.println(invoice.getTitle() + " " + invoice.getPages());
    }
}
