/*
 * ============================================================================
 * Problem 20 (Google L4 prep) - FOLLOW-UP 4: Max weighted positive component.
 * ============================================================================
 *
 * PROMPT
 *   Node values are now arbitrary weights instead of 0/1. A node belongs to
 *   a component if it is connected through nodes whose weight is positive.
 *   Return the maximum sum of weights over all such components.
 *
 * EXAMPLE
 *        0
 *       /  \
 *      5    0
 *     / \
 *    3   2
 *   -> component {5,3,2} sums to 10 -> answer = 10
 *
 * INTUITION
 *   Identical DFS-aggregation skeleton as the base problem: only the
 *   membership test changes from `node.val == 1` to `node.val > 0`, and
 *   instead of counting nodes we sum their weights.
 *
 * ALGORITHM
 *   1. dfs(node) returns the total weight of the positive component
 *      rooted downward at `node` (0 if node is null or node.val <= 0,
 *      since such a node can't extend a component upward).
 *   2. For a positive node, currentSum = node.val + leftSum + rightSum;
 *      update the global max with currentSum, then return currentSum so
 *      the parent can extend the component further.
 *
 * COMPLEXITY
 *   Time O(N): each node visited once.
 *   Space O(H): recursion stack, H = tree height.
 * ============================================================================
 */
public class MaxWeightedComponent {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    private int maxComponentSum;

    public int findMaxComponentSum(TreeNode root) {
        maxComponentSum = 0;
        dfs(root);
        return maxComponentSum;
    }

    // Returns the total weight of the positive component rooted downward
    // at `node` (0 if node is null or non-positive, breaking the chain).
    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftSum = dfs(node.left);
        int rightSum = dfs(node.right);

        if (node.val <= 0) {
            return 0;
        }

        int currentSum = node.val + leftSum + rightSum;
        maxComponentSum = Math.max(maxComponentSum, currentSum);
        return currentSum;
    }

    public static void main(String[] args) {
        MaxWeightedComponent solution = new MaxWeightedComponent();

        // 0 -> {5,3,2} left component sums to 10; right 0 is ignored.
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(5);
        root.right = new TreeNode(0);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(2);
        check("example tree", solution.findMaxComponentSum(root), 10);

        // Two separated positive components: {5,3,2}=10 vs {4}=4 -> 10 wins.
        TreeNode root2 = new TreeNode(0);
        root2.left = new TreeNode(5);
        root2.left.left = new TreeNode(3);
        root2.left.right = new TreeNode(2);
        root2.right = new TreeNode(0);
        root2.right.right = new TreeNode(4);
        check("two separated components", solution.findMaxComponentSum(root2), 10);

        // A negative node breaks the chain; only the surviving side counts.
        TreeNode root3 = new TreeNode(5);
        root3.left = new TreeNode(-3);
        root3.right = new TreeNode(2);
        check("negative node breaks chain", solution.findMaxComponentSum(root3), 7);

        // Single positive leaf.
        check("single positive node", solution.findMaxComponentSum(new TreeNode(9)), 9);

        // All non-positive: no component exists, answer is 0.
        TreeNode allNonPositive = new TreeNode(0);
        allNonPositive.left = new TreeNode(-1);
        allNonPositive.right = new TreeNode(-2);
        check("all non-positive", solution.findMaxComponentSum(allNonPositive), 0);

        check("empty", solution.findMaxComponentSum(null), 0);

        System.out.println("all passed");
    }

    private static void check(String name, int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual
                    + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
