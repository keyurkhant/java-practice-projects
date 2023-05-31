public class AmortizedTree implements Searchable, TreeDebug {
    private Node root;
    private String[] waitingArray;
    private int treeSize;
    private int waitingArraySize;
    private int waitingIdx;
    private boolean rebalanced;

    /* Constructor for AmortizedTree, initialized all internal variables. */
    AmortizedTree() {
        root = null;
        waitingArray = new String[1];
        waitingArraySize = 1;
        treeSize = 0;
        waitingIdx = 0;
        rebalanced = true;
    }

    /**
     *
     * @param key - String to add into the waiting array and if array become full after adding the key, then insert array to the tree
     * @return - true if element added successfully else false
     */
    @Override
    public boolean add(String key) {
        if (key == null) return false;

        if (find(key)) return false;

        addToWaitingArray(key);
        if (waitingIdx == waitingArraySize) {
            insertFromWaitingArray(waitingArray,0, waitingIdx - 1);
            waitingIdx = 0;
            resizeInsertionArray();
            return true;
        }
        return true;
    }

    /**
     *
     * @param key - String key to add to waiting array
     */
    private void addToWaitingArray(String key) {
        int i;
        int last = waitingIdx - 1;
        for(i = 0; i < waitingIdx; i++) {
            if(waitingArray[i].compareTo(key) > 0) {
                break;
            }
        }
        while(last >= i) {
            waitingArray[last+1] = waitingArray[last];
            last--;
        }
        waitingArray[i] = key;
        waitingIdx++;
    }

    /**
     *
     * @param arr - Array of string from which elements will be inserted to the tree
     * @param start - Starting index of the array from which to start inserting element
     * @param end - Ending index of the array till which elements will be inserted
     */
    private void insertFromWaitingArray(String arr[], int start, int end) {
        if (start > end) {
            return;
        }

        int middle = (start + end) / 2;
        insertToTree(arr[middle], root);
        insertFromWaitingArray(arr, start, middle - 1);
        insertFromWaitingArray(arr,middle + 1, end);
    }

    /**
     *
     * resizing the array
     */
    private void resizeInsertionArray() {
        waitingArraySize = (int) Math.ceil(Math.log(treeSize + 1) / Math.log(2));
        String[] newArray = new String[waitingArraySize];
        System.arraycopy(waitingArray, 0, newArray, 0, waitingIdx);
        waitingArray = newArray;
    }

    /**
     *
     * @param key - Insert the String key to the tree
     * @param node - Root node of the tree to insert the element
     * @return - return true is element added to tree successfully else false
     */
    private boolean insertToTree(String key, Node node) {
        if(node == null) {
            root = new Node(key);
            treeSize++;
            return true;
        }
        if (key.equals(node.key)) {
            return false;
        } else if (key.compareTo(node.key) < 0) {
            if (node.left == null) {
                node.left = new Node(key);
                return true;
            } else {
                return insertToTree(key, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new Node(key);
                return true;
            } else {
                return insertToTree(key, node.right);
            }
        }
    }

    /**
     *
     * @param key - String key to search in the tree or waitingArray
     * @return - return true if element exist in tree of waitingArray else false
     */
    @Override
    public boolean find(String key) {
        Node node = findInTree(key, root);
        if(node == null) return false;
        else return node != null || findInWaitingArray(key);
    }

    /**
     *
     * @param key - Search the key in the tree
     * @param current - root element of the tree
     * @return - return the node of the tree
     */
    private Node findInTree(String key, Node current) {
        if(current == null) return null;
        else if (key.equals(current.key)) {
            return new Node(key);
        } else if (key.compareTo(current.key) < 0) {
            return findInTree(key, current.left);
        } else {
            return findInTree(key, current.right);
        }
    }

    /**
     *
     * @param key - Search the key in the waitingArray
     * @return - return true if element exists in the waitingArray else false
     */
    private boolean findInWaitingArray(String key) {
        int left = 0;
        int right = waitingIdx - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;
            int compare = key.compareTo(waitingArray[middle]);
            if (compare == 0) {
                // middle value
                return true;
            } else if (compare < 0) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return false;
    }

    /**
     *
     * @param key - Remove string key from the tree
     * @return - return true is elemenet is removed sucessfullly else false
     */
    @Override
    public boolean remove(String key) {
        Node node = deleteNode(root, key);
        if(node == null) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param key - Remove string key from the tree
     * @return - return the deleted node of the tree
     */
    public Node deleteNode(Node node, String key) {
        if(node == null) return null;
        if(key.compareTo(node.key) > 0) {
            node.right = deleteNode(node.right, key);
        } else if (key.compareTo(node.key) < 0) {
            node.left = deleteNode(node.left, key);
        } else if (key.compareTo(node.key) == 0) {
            if(node.left == null && node.right == null) {
                node = null;
            } else if (node.right != null) {
                node.key = successor(node);
                node.right = deleteNode(node.right, node.key);
            } else {
                node.key = predecessor(node);
                node.left = deleteNode(node.left, node.key);
            }
        }
        return node;
    }

    /**
     *
     * @param root - root node of the tree
     * @return - return successor of the tree
     */
    private String successor(Node root){
        root = root.right;
        while(root.left != null){
            root = root.left;
        }
        return root.key;
    }

    /**
     *
     * @param root - root node of the tree
     * @return - return predecessor of the tree
     */
    private String predecessor(Node root){
        root = root.left;
        while(root.right != null){
            root = root.right;
        }
        return root.key;
    }

    /**
     *
     * @return - size of the tree
     */
    @Override
    public int size() {
        return countTreeNodes(root) + waitingIdx;
    }

    /**
     *
     * @return - count of the tree node
     */
    private int countTreeNodes(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countTreeNodes(node.left) + countTreeNodes(node.right);
    }

    /**
     *
     * @return - return true if tree rebalance successfully else false
     */
    @Override
    public boolean rebalance() {
        String[] values = treeValues();
        if (values == null) {
            return false;
        }

        root = null;
        insertFromWaitingArray(values,0, values.length - 1);

        return true;
    }

    /**
     *
     * @param key - String to rebalance the tree
     * @return - return true if tree rebalance for the key is successful else false
     */
    @Override
    public boolean rebalanceValue(String key) {
        Node node = findInTree(key, root);

        if (node == null) {
            return false;
        }

        String[] values = treeValues();
        if (values == null) {
            return false;
        }

        root = node;
        insertFromWaitingArray(values, 0, values.length - 1);

        return true;
    }

    /**
     *
     * @return - return string to pring tree
     */
    @Override
    public String printTree() {
        String treeString = "";
        try {
            String[] values = treeValues();
            for(String value: values) {
                treeString += value + " " + depth(value) + "\n";
            }
        } catch (Exception e) {
            return null;
        }
        return treeString;
    }

    /**
     *
     * @return - return list of elements in waitingArray
     */
    @Override
    public String[] awaitingInsertion() {
        return waitingArray;
    }

    /**
     *
     * @return - return array of the inorder traversal of the tree
     */
    @Override
    public String[] treeValues() {
        String[] values = new String[size()];
        int index = 0;
        inOrderTraversal(root, values, index);
        return values;
    }

    /**
     *
     * @return - return array of the inorder traversal of the tree
     */
    private int inOrderTraversal(Node node, String[] values, int index) {
        if (node != null) {
            index = inOrderTraversal(node.left, values, index);
            values[index++] = node.key;
            index = inOrderTraversal(node.right, values, index);
        }
        return index;
    }

    /**
     *
     * @return - return depth of the tree
     */
    @Override
    public int depth(String key) {
        Node current = root;
        int level = 1;
        while (current != null) {
            if (key.equals(current.key)) {
                return level;
            } else if (key.compareTo(current.key) < 0) {
                current = current.left;
                level++;
            } else {
                current = current.right;
                level++;
            }
        }
        return size() + 1;
    }
}
