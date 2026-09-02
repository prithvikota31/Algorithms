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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConnectedOneComponents {

    public static class TreeNode {
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
        dfs(root, 0);
        return new int[] {componentCount, largestComponentSize};
    }

    //this returns total connect 1 components including this root 1
    //returns 0 if current node/root is zero
    private int dfs(TreeNode node, int parent) //returns considering that as root
    {
        if(node == null)
        {
            return 0;
        }
        //even if node value is 0, it has to go to left and right
        int left = dfs(node.left, node.val);
        int right = dfs(node.right, node.val);

        if(node.val == 1 && parent == 0)
        {
            componentCount++;
        }

        if(node.val == 0)
        {
            return 0;
        }
        else
        {
            int currentComponentSize = 1 + left + right;
            largestComponentSize = Math.max(largestComponentSize, currentComponentSize);
            return currentComponentSize;
        }

    }

    /*
     * ============================================================================
     * FOLLOW-UP 1 (gci #20) - Return the largest component's NODES, not just size.
     * ============================================================================
     *
     * INTUITION
     *   Building a new list at every node and merging children into it (via
     *   addAll) recopies each node once per ancestor above it -> O(N log N) for
     *   balanced trees, O(N^2) worst case for a skewed tree. Avoid that by
     *   splitting into two O(N) passes: first find WHICH node roots the
     *   largest component using plain ints (no lists), then collect nodes
     *   from just that one subtree.
     *
     * ALGORITHM
     *   1. sizeDfs: same size computation as the base problem, but remember
     *      the node (bestRoot) whenever a new largest size is seen.
     *   2. collect: one more DFS, run only from bestRoot, appending every
     *      node in that component (stops at any 0-node boundary).
     *
     * COMPLEXITY
     *   Time O(N): pass 1 touches every node once with O(1) work each; pass 2
     *   touches only the winning component once.
     *   Space O(N) worst case, for the returned node list.
     * ============================================================================
     */
    private int bestSize;
    private TreeNode bestRoot;

    public List<TreeNode> findLargestComponentNodes(TreeNode root) {
        List<TreeNode> result = new ArrayList<>();
        bestRoot = null;
        bestSize = 0;
        //first find the best node
        findLargestComponentNodesHelper(root);

        collectNodes(bestRoot, result);
        return result;
    }

    private void collectNodes(TreeNode node, List<TreeNode> result)
    {
        if(node == null)
        {
            return;
        }
        if(node.val == 0)
        {
            return;
        }

        result.add(node);
        collectNodes(node.left, result);
        collectNodes(node.right, result);
    }

    private int findLargestComponentNodesHelper(TreeNode node)
    {
        if(node == null)
        {
            return 0;
        }

        int left = findLargestComponentNodesHelper(node.left);
        int right = findLargestComponentNodesHelper(node.right);

        int curComponentSize = 0;
        if(node.val == 0)
        {
            return 0;
        }
        else
        {
            curComponentSize = 1 + left + right;
            if(curComponentSize > bestSize)
            {
                bestRoot = node;
                bestSize = curComponentSize;
            }
            return curComponentSize;
        }
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

        // Follow-up 1: largest component's nodes (values, in DFS order).
        checkNodes("largest component nodes", solution.findLargestComponentNodes(root),
                Arrays.asList(1, 1, 1));
        checkNodes("largest component nodes (all ones)",
                solution.findLargestComponentNodes(allOnes), Arrays.asList(1, 1, 1));
        checkNodes("largest component nodes (all zeros)",
                solution.findLargestComponentNodes(allZeros), Collections.emptyList());
        checkNodes("largest component nodes (empty)",
                solution.findLargestComponentNodes(null), Collections.emptyList());

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

    private static void checkNodes(String name, List<TreeNode> actual, List<Integer> expectedValues) {
        List<Integer> actualValues = new ArrayList<>();
        for (TreeNode node : actual) {
            actualValues.add(node.val);
        }
        if (!actualValues.equals(expectedValues)) {
            throw new AssertionError("FAIL " + name + ": got " + actualValues
                    + " want " + expectedValues);
        }
        System.out.println("pass " + name + " -> " + actualValues);
    }
}