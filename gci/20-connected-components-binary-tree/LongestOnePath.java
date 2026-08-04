/*
 * ============================================================================
 * Problem 20 (Google L4 prep) - FOLLOW-UP 3: Longest path through only 1s.
 * ============================================================================
 *
 * PROMPT
 *   Given a binary tree whose values are 0 or 1, return the length (node
 *   count) of the longest path where every node on the path has value 1.
 *   A path may go down-through-up via a common ancestor (not just straight
 *   down one branch). 0-nodes break the path; the path cannot cross them.
 *
 * EXAMPLE
 *      1
 *     / \
 *    1   1
 *   -> longest path = 3 (left -> root -> right)
 *
 * INTUITION
 *   This is the classic "tree diameter" pattern with one extra rule: a
 *   0-node acts as a cut point and contributes 0 to any path through it.
 *   For a 1-node, the longest DOWNWARD chain of 1s through it is
 *   1 + max(leftChain, rightChain) -- that's what a parent can build on.
 *   But the longest PATH through it (not continuing upward) can use BOTH
 *   children's chains at once: leftChain + 1 + rightChain.
 *
 * ALGORITHM
 *   1. DFS returns the longest downward 1-chain starting at `node`
 *      (0 if node is null or node.val == 0 -- it can't extend a chain).
 *   2. At each 1-node, combine both children's chains through this node to
 *      update the global longest path answer.
 *   3. Return only 1 + max(leftChain, rightChain) upward, since a path
 *      continuing through the parent can only use one branch.
 *
 * COMPLEXITY
 *   Time O(N): each node visited once.
 *   Space O(H): recursion stack, H = tree height.
 * ============================================================================
 */
public class LongestOnePath {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    private int longestPath;

    public int findLongestPath(TreeNode root) {
        longestPath = 0;
        dfs(root);
        return longestPath;
    }

    // Returns the longest downward chain of 1s starting at `node` (0 if
    // node is null or a 0, since it can't extend a chain upward).
    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftChain = dfs(node.left);
        int rightChain = dfs(node.right);

        if (node.val == 0) {
            return 0;
        }

        int pathThroughNode = leftChain + 1 + rightChain;
        longestPath = Math.max(longestPath, pathThroughNode);

        return 1 + Math.max(leftChain, rightChain);
    }

    public static void main(String[] args) {
        LongestOnePath solution = new LongestOnePath();

        // Follow-up 1's separated-components tree: longest 1-path is
        // left.left -> left -> ... but left's siblings are 1 too, so
        // trace carefully per tree below.
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(1);
        root.right = new TreeNode(0);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(1);
        root.right.right = new TreeNode(1);
        root.right.right.left = new TreeNode(1);
        // Component 1: left.left -> left -> left.right = 3 nodes.
        // Component 2: right.right.left -> right.right = 2 nodes.
        check("separated components", solution.findLongestPath(root), 3);

        // All-1 tree: left.left -> left -> root -> right = 4 nodes.
        TreeNode allOnes = new TreeNode(1);
        allOnes.left = new TreeNode(1);
        allOnes.right = new TreeNode(1);
        allOnes.left.left = new TreeNode(1);
        check("all ones with extra depth", solution.findLongestPath(allOnes), 4);

        TreeNode allZeros = new TreeNode(0);
        allZeros.left = new TreeNode(0);
        allZeros.right = new TreeNode(0);
        check("all zeros", solution.findLongestPath(allZeros), 0);

        check("empty", solution.findLongestPath(null), 0);

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
