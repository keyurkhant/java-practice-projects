public class Node {
    /*
    * Node class represent node of AmortizedTree. Each node contains key and will set left and right nodes.
    */
    String key;
    Node left;
    Node right;

    Node(String key) {
        this.key = key;
        left = null;
        right = null;
    }
}
