import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================================
 * Problem 20 (Google L4 prep) - FOLLOW-UP 3b: Longest 1-path, actual nodes.
 * ============================================================================
 *
 * PROMPT
 *   Follow-up 3 found the LENGTH of the longest path through only 1-nodes.
 *   Now return the actual list of nodes on that path, in order.
 *
 * EXAMPLE
 *        1
 *      /   \
 *     1     1
 *    /
 *   1
 *   -> longest path = [left.left, left, root, right]
 *
 * INTUITION
 *   Same diameter pattern as follow-up 3, but naively rebuilding the path
 *   with addAll()/reverse() at every node re-copies each node's chain once
 *   per ancestor -> O(N^2) on a skewed tree (the same anti-pattern flagged
 *   for follow-ups 1/2). Avoid it with a two-pass approach:
 *     Pass 1: DFS computes, for every node, only the downward 1-chain
 *              LENGTH (an int, memoized in a map) and tracks the single
 *              "turning point" node where leftChain + 1 + rightChain is
 *              largest -- no lists built yet.
 *     Pass 2: Walk down from the turning point once on each side, using
 *              the memoized lengths to pick the longer child at each step
 *              (no recomputation, no re-copying). This walk is O(H) per
 *              side, not O(N).
 *
 * ALGORITHM
 *   1. computeDownLen(node): postorder DFS. For a 0-node (or null), the
 *      chain length is 0. For a 1-node, downLen = 1 + max(leftLen,
 *      rightLen); also check leftLen + 1 + rightLen against the best
 *      path length seen so far, recording this node as bestNode if larger.
 *   2. Reconstruct: follow the left child chain from bestNode.left down
 *      (picking whichever child has the larger memoized downLen at each
 *      step, stopping at null/a 0-node), reverse that chain, add
 *      bestNode, then follow the right child chain down the same way.
 *
 * COMPLEXITY
 *   Time O(N): pass 1 visits every node once; pass 2 walks two root-to-leaf
 *              paths, O(H) each, using only memoized lengths.
 *   Space O(N): the downLen map (O(H) extra for recursion/walk stacks).
 * ============================================================================
 */
public class LongestOnePathNodes {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    private final Map<TreeNode, Integer> downLen = new HashMap<>();
    private TreeNode bestNode;
    private int bestLength;

    public List<TreeNode> findLongestPath(TreeNode root) {
        bestNode = null;
        bestLength = 0;
        downLen.clear();
        computeDownLen(root);

        List<TreeNode> result = new ArrayList<>();
        if (bestNode == null) {
            return result;
        }

        List<TreeNode> leftChain = new ArrayList<>();
        walkChain(bestNode.left, leftChain);
        Collections.reverse(leftChain);

        result.addAll(leftChain);
        result.add(bestNode);
        walkChain(bestNode.right, result);
        return result;
    }

    // Returns the longest downward chain length of 1s starting at `node`
    // (0 if node is null or a 0, since it can't extend a chain upward).
    private int computeDownLen(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftLen = computeDownLen(node.left);
        int rightLen = computeDownLen(node.right);

        if (node.val == 0) {
            downLen.put(node, 0);
            return 0;
        }

        int pathThroughNode = 1 + leftLen + rightLen;
        if (pathThroughNode > bestLength) {
            bestLength = pathThroughNode;
            bestNode = node;
        }

        int down = 1 + Math.max(leftLen, rightLen);
        downLen.put(node, down);
        return down;
    }

    // Appends node, then its child, then its grandchild, ... (top-down),
    // always following whichever child has the larger memoized downLen.
    private void walkChain(TreeNode node, List<TreeNode> out) {
        TreeNode cur = node;
        while (cur != null && downLen.get(cur) > 0) {
            out.add(cur);
            int leftLen = cur.left == null ? 0 : downLen.get(cur.left);
            int rightLen = cur.right == null ? 0 : downLen.get(cur.right);
            cur = (leftLen >= rightLen) ? cur.left : cur.right;
        }
    }
    public static void main(String[] args) {
        LongestOnePathNodes solution = new LongestOnePathNodes();

        // All-1 tree: left.left -> left -> root -> right = 4 nodes.
        TreeNode leaf = new TreeNode(1);
        TreeNode left = new TreeNode(1);
        left.left = leaf;
        TreeNode right = new TreeNode(1);
        TreeNode root = new TreeNode(1);
        root.left = left;
        root.right = right;
        check("all ones with extra depth", solution.findLongestPath(root),
                Arrays.asList(leaf, left, root, right));

        // Separated components tree (same as follow-up 3): best path is
        // left.left -> left -> left.right = 3 nodes (found first, so it
        // wins the tie over the 2-node right-side component).
        TreeNode leftLeft = new TreeNode(1);
        TreeNode leftRight = new TreeNode(1);
        TreeNode midLeft = new TreeNode(1);
        midLeft.left = leftLeft;
        midLeft.right = leftRight;
        TreeNode rightRightLeft = new TreeNode(1);
        TreeNode rightRight = new TreeNode(1);
        rightRight.left = rightRightLeft;
        TreeNode midRight = new TreeNode(0);
        midRight.right = rightRight;
        TreeNode root2 = new TreeNode(0);
        root2.left = midLeft;
        root2.right = midRight;
        check("separated components", solution.findLongestPath(root2),
                Arrays.asList(leftLeft, midLeft, leftRight));

        TreeNode allZeros = new TreeNode(0);
        allZeros.left = new TreeNode(0);
        allZeros.right = new TreeNode(0);
        check("all zeros", solution.findLongestPath(allZeros),
                Collections.emptyList());

        check("empty", solution.findLongestPath(null), Collections.emptyList());

        System.out.println("all passed");
    }

    private static void check(String name, List<TreeNode> actual, List<TreeNode> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got size " + actual.size()
                    + " want size " + expected.size());
        }
        System.out.println("pass " + name + " -> size " + actual.size());
    }
}
