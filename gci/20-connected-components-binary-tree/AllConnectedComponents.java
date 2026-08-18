import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * ============================================================================
 * Problem 20 (Google L4 prep) - FOLLOW-UP 2: Return ALL connected components.
 * ============================================================================
 *
 * PROMPT
 *   Given a binary tree whose values are 0 or 1, return every connected
 *   component of 1-nodes as a list of its actual nodes (not just the count
 *   or the size of the largest one).
 *
 * EXAMPLE
 *           0
 *         /   \
 *        1     0
 *       / \     \
 *      1   1     1
 *               /
 *              1
 *   -> [[node(1) at left, node(1) left.left, node(1) left.right],
 *       [node(1) at right.right, node(1) at right.right.left]]
 *
 * INTUITION
 *   Same trap as follow-up 1: building a new list at every node and merging
 *   children into it (addAll) recopies each node once per ancestor above it
 *   -> O(N log N) balanced / O(N^2) skewed worst case. Split into two O(N)
 *   passes instead: first find every component's root node (a 1-node with
 *   no 1-parent), then run one independent DFS per root to collect its
 *   nodes. Components are disjoint, so summed collection work is O(N).
 *
 * ALGORITHM
 *   1. findComponentRoots: DFS while tracking parentIsOne; whenever a 1-node
 *      has no 1-parent, record it as a component root.
 *   2. For each recorded root, run collectComponent (DFS that stops at any
 *      0/null boundary) to gather that component's nodes into its own list.
 *
 * COMPLEXITY
 *   Time O(N): pass 1 visits every node once with O(1) work; pass 2's total
 *   work across all components is O(N) since components are disjoint.
 *   Space O(N) for the returned components (plus O(H) recursion stack).
 * ============================================================================
 */
public class AllConnectedComponents {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public List<List<TreeNode>> findAllComponents(TreeNode root) {
        List<List<TreeNode>> allComponents = new ArrayList<>();
        List<TreeNode> componentRoots = new ArrayList<>();
        findComponentRoots(root, 0, componentRoots);

        for(TreeNode cRoot: componentRoots)
        {
            List<TreeNode> curComponent = new ArrayList<>();

            collectComponent(cRoot, curComponent);
            allComponents.add(curComponent);
        }
        return allComponents;
    }

    private void findComponentRoots(TreeNode node, int parent, List<TreeNode> componentRoots)
    {
        if(node == null)
        {
            return;
        }

        if(node.val == 1 && parent == 0)
        {
            componentRoots.add(node);
        }
        
        findComponentRoots(node.left, node.val, componentRoots);
        findComponentRoots(node.right, node.val, componentRoots);
    }

    private void collectComponent(TreeNode node, List<TreeNode> componentList)
    {
        if(node == null || node.val == 0)
        {
            return;
        }

        componentList.add(node);
        collectComponent(node.left, componentList);
        collectComponent(node.right, componentList);

    }





    public static void main(String[] args) {
        AllConnectedComponents solution = new AllConnectedComponents();

        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(1);
        root.right = new TreeNode(0);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(1);
        root.right.right = new TreeNode(1);
        root.right.right.left = new TreeNode(1);
        checkComponents("separated components", solution.findAllComponents(root),
                Arrays.asList(Arrays.asList(1, 1, 1), Arrays.asList(1, 1)));

        TreeNode allOnes = new TreeNode(1);
        allOnes.left = new TreeNode(1);
        allOnes.right = new TreeNode(1);
        checkComponents("all ones", solution.findAllComponents(allOnes),
                Arrays.asList(Arrays.asList(1, 1, 1)));

        TreeNode allZeros = new TreeNode(0);
        allZeros.left = new TreeNode(0);
        allZeros.right = new TreeNode(0);
        checkComponents("all zeros", solution.findAllComponents(allZeros),
                Arrays.asList());

        checkComponents("empty", solution.findAllComponents(null), Arrays.asList());

        System.out.println("all passed");
    }

    private static void checkComponents(String name, List<List<TreeNode>> actual,
            List<List<Integer>> expectedValues) {
        List<List<Integer>> actualValues = new ArrayList<>();
        for (List<TreeNode> component : actual) {
            List<Integer> values = new ArrayList<>();
            for (TreeNode node : component) {
                values.add(node.val);
            }
            actualValues.add(values);
        }
        if (!actualValues.equals(expectedValues)) {
            throw new AssertionError("FAIL " + name + ": got " + actualValues
                    + " want " + expectedValues);
        }
        System.out.println("pass " + name + " -> " + actualValues);
    }
}
