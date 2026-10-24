package patterns.composite;

public class EntryPoint {
    public static void main(String[] args) {
        Directory root = new Directory("root")
                .add(new File("a.txt", 100))
                .add(new Directory("docs").add(new File("b.pdf", 300)));
        System.out.println(root.getName() + " = " + root.getSize() + " bytes");
    }
}
