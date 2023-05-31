public class Main {
    public static void main(String[] args) {
        AmortizedTree tree = new AmortizedTree();
        String values[] = {"E", "A", "B", "Z", "C"};

        for(String value:values){
            tree.add(value);
        }

        System.out.println("TREE SIZE======>"+ tree.size());
        System.out.println("DEPTH OF THE KEY(AVAILABLE)======>"+ tree.depth("B"));
        System.out.println("DEPTH OF THE KEY(UNAVAILABLE)======>"+ tree.depth("299"));
        System.out.println("TREE PRINT 1=======>" + tree.printTree());

        // Added new value
        System.out.println("IS NEW ELEMENT ADDED===>"+ tree.add("D"));

        System.out.println("RE BALANCE=======>" + tree.rebalance());
        System.out.println("TREE PRINT 2=======>" + tree.printTree());
        System.out.println("IS ELEMENT REMOVED====>" + tree.remove("C"));
        System.out.println("RE BALANCE=======>" + tree.rebalance());
        System.out.println("TREE PRINT 3=======>" + tree.printTree());
    }
}