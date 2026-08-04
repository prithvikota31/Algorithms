/*
 * ============================================================================
 * Problem 20 (Google L4 prep) - Connected Components of 1s in a Binary Tree
 * ============================================================================
 *
 * PROMPT
 *   Given a binary tree whose values are 0 or 1, return:
 *     1. the number of connected components containing only 1-nodes, and
 *     2. the size of the largest such component.
 *   Nodes are connected through parent-child edges. A 0-node is a separator.
 *
 * EXAMPLES
 *   [0, 1, 0, 1, 1, null, 1] -> count 2, largest 3
 *   [1, 1, 1]                 -> count 1, largest 3
 *   [0, 0, 0] or empty tree   -> count 0, largest 0
 *
 * INTUITION
 *   A component starts exactly where a 1-node does not have a 1-parent.
 *   DFS carries that one bit of parent state downward to count starts.
 *   On the way back up, each 1-node returns the size of its connected
 *   1-subtree, so the largest component can be updated at every node.
 *
 * ALGORITHM
 *   1. DFS from the root while passing whether the parent is a 1-node.
 *   2. At a 1-node with no 1-parent, increment the component count.
 *   3. Recursively get the connected sizes from both children.
 *   4. A 0-node returns 0; a 1-node returns 1 + leftSize + rightSize.
 *   5. Track the largest returned size.
 *
 * COMPLEXITY
 *   Time O(N), because each node is visited once.
 *   Space O(H), where H is the tree height, for the recursion stack.
 * ============================================================================
 */
public class ConnectedOneComponents {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    private int componentCount;
    private int largestComponentSize;

    public int[] findComponents(TreeNode root) {
        componentCount = 0;
        largestComponentSize = 0;

        connectedSize(root, false);
        return new int[] {componentCount, largestComponentSize};
    }

    private int connectedSize(TreeNode node, boolean parentIsOne) {
        if (node == null) {
            return 0;
        }

        boolean nodeIsOne = node.val == 1;
        if (nodeIsOne && !parentIsOne) {
            componentCount++;
        }

        int leftSize = connectedSize(node.left, nodeIsOne);
        int rightSize = connectedSize(node.right, nodeIsOne);

        if (!nodeIsOne) {
            return 0;
        }

        int currentSize = 1 + leftSize + rightSize;
        largestComponentSize = Math.max(largestComponentSize, currentSize);
        return currentSize;
    }

    public static void main(String[] args) {
        ConnectedOneComponents solution = new ConnectedOneComponents();

        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(1);
        root.right = new TreeNode(0);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(1);
        root.right.right = new TreeNode(1);
        root.right.right.left = new TreeNode(1);
        check("separated components", solution.findComponents(root), 2, 3);

        TreeNode allOnes = new TreeNode(1);
        allOnes.left = new TreeNode(1);
        allOnes.right = new TreeNode(1);
        check("all ones", solution.findComponents(allOnes), 1, 3);

        TreeNode allZeros = new TreeNode(0);
        allZeros.left = new TreeNode(0);
        allZeros.right = new TreeNode(0);
        check("all zeros", solution.findComponents(allZeros), 0, 0);

        check("empty", solution.findComponents(null), 0, 0);
        System.out.println("all passed");
    }

    private static void check(String name, int[] actual, int expectedCount,
            int expectedLargest) {
        if (actual[0] != expectedCount || actual[1] != expectedLargest) {
            throw new AssertionError("FAIL " + name + ": got count=" + actual[0]
                    + ", largest=" + actual[1] + "; want count=" + expectedCount
                    + ", largest=" + expectedLargest);
        }
        System.out.println("pass " + name + " -> count=" + actual[0]
                + ", largest=" + actual[1]);
    }
}