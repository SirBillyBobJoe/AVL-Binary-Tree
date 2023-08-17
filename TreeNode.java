import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    TreeNode parent;
    TreeNode left;
    TreeNode right;
    int val;
    int height;
    static TreeNode root;

    public TreeNode(int val) {
        this.val = val;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.height = 1;
        if (root == null) {
            root = this;
        }
    }

    // rotates tree to the left
    public TreeNode rotateLeft(TreeNode node) {
        TreeNode rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;
        rightChild.left = node;
        node.parent = rightChild;

        updateHeight(node);
        updateHeight(rightChild);

        return rightChild;
    }

    // rotates tree right
    public TreeNode rotateRight(TreeNode node) {
        TreeNode leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;
        leftChild.right = node;
        node.parent = leftChild;

        updateHeight(node);
        updateHeight(leftChild);

        return leftChild;
    }

    public void add(int val) {
        root = addRecursively(root, val);
    }

    private TreeNode addRecursively(TreeNode node, int val) {
        // sets root to new val if the node is empty
        if (node == null) {
            return new TreeNode(val);
        }
        // go left if less than the node value
        if (val < node.val) {
            node.left = addRecursively(node.left, val);
            node.left.parent = node;
        } else if (val > node.val) {
            // go right if more than node value
            node.right = addRecursively(node.right, val);
            node.right.parent = node;
        } else {
            // Ignore the duplicate value
            return node;
        }

        // increases the height of the current node after the recursion
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // determines the balance between the left and right nodes
        int balance = balanceFactor(node);

        return rotateHelper(node, balance, val);

    }

    public void remove(int val) {
        root = removeRecursively(root, val);
    }

    private TreeNode removeRecursively(TreeNode node, int val) {
        // if node is null return null
        if (node == null) {
            return node;
        }

        // recursively search for node to delete
        if (val < node.val) {
            node.left = removeRecursively(node.left, val);
        } else if (val > node.val) {
            node.right = removeRecursively(node.right, val);
        } else {
            // node with only one or no child
            if (node.left == null || node.right == null) {
                TreeNode temp = node.left != null ? node.left : node.right;
                // no child
                if (temp == null) {
                    temp = node;
                    node = null;
                } else { // one child
                    node = temp;
                }
                temp = null;
            } else {// node with 2 childs
                TreeNode temp = minValueNode(node.right);
                node.val = temp.val;
                node.right = removeRecursively(node.right, temp.val);
            }
        }

        if (node == null) {
            return node;
        }

        // Update the height and parent pointers
        node.height = 1 + Math.max(height(node.left), height(node.right));
        if (node.left != null)
            node.left.parent = node;
        if (node.right != null)
            node.right.parent = node;

        // balances the tree and rotates it
        int balance = balanceFactor(node);
        return rotateHelper(node, balance, val);
    }

    // finds node with smallest value
    private TreeNode minValueNode(TreeNode node) {
        TreeNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // determines if current state of tree needs to be rotated
    private TreeNode rotateHelper(TreeNode node, int balance, int val) {
        // Left-Left Case:S perform right rotation.
        if (balance > 1 && val < node.left.val) {
            return rotateRight(node);
        }
        // Right-Right Case: perform left rotation.
        if (balance < -1 && val > node.right.val) {
            return rotateLeft(node);
        }
        // Left-Right Case: perform left rotation on the left child, then right
        // rotation.
        if (balance > 1 && val > node.left.val) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
            // Right-Left Case: perform right rotation on the right child, then left
            // rotation.
        }
        if (balance < -1 && val < node.right.val) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private void updateHeight(TreeNode node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // returns the height of a given node
    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        } else {
            return node.height;
        }
    }

    // determines the balance between the left and right child
    private int balanceFactor(TreeNode node) {
        if (node == null) {
            return 0;
        } else {
            return height(node.left) - height(node.right);
        }
    }

    public void printTree() {
        printTreeHelper(root, "", true);
    }

    private void printTreeHelper(TreeNode node, String prefix, boolean isTail) {
        if (node == null)
            return;

        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.val);
        List<TreeNode> children = new ArrayList<>();
        if (node.right != null)
            children.add(node.right);
        if (node.left != null)
            children.add(node.left);
        for (int i = 0; i < children.size() - 1; i++) {
            printTreeHelper(children.get(i), prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            printTreeHelper(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "), true);
        }
    }

    public static void main(String[] args) {
        TreeNode tree = new TreeNode(1);
        for (int i = 2; i <= 20; i++) {
            tree.add(i);
        }
        tree.remove(10);
        tree.remove(6);
        tree.printTree();

    }

}