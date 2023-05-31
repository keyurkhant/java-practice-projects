public interface TreeDebug {
    String printTree();
    String[] awaitingInsertion();
    String[] treeValues();
    int depth(String key);
}
