import java.util.*;

/*
 * Problem 19 — Process tree leaves based on maximum ancestor values.
 *
 * PROMPT:
 *   Given a binary tree, return the values of all leaves that are strictly
 *   greater than every ancestor on their own root-to-leaf path.
 *
 * EXAMPLE:
 *           5
 *          / \
 *         3   8
 *        / \   \
 *       6   4   9
 *   paths: 5->3->6 (maxAnc 5, 6>5 keep) | 5->3->4 (maxAnc 5, 4<5 drop)
 *          5->8->9 (maxAnc 8, 9>8 keep)
 *   output: [6, 9]
 *
 * MENTAL MAP:
 *   DFS + path state carried DOWNWARD. A leaf's decision depends on its
 *   history (was anything bigger above it?), which the leaf can't see on its
 *   own — so thread `maxAncestor` (max value from root down to the PARENT)
 *   into the recursion. Moving to a child: newMax = max(maxAncestor, node.val).
 *
 * COMPLEXITY: O(N) time, O(H) space.
 */
public class TreeLeavesMaxAncestor {

    static class TreeNode {
        int val;
        TreeNode left, right;
        public TreeNode(int val) 
        { 
            this.val = val; 
        }
    }

    public List<Integer> findSpecialLeaves(TreeNode root) {
        //send max along nodes
        List<Integer> result = new ArrayList<>();
        if(root == null || isLeaf(root))
        {
            return result;
        }
        dfs(root, result, Integer.MIN_VALUE);
        return result;
    }

    private void dfs(TreeNode node, List<Integer> result, int maxSoFar)
    {
        if(node == null)
        {
            return;
        }
        if(isLeaf(node))
        {
            if(node.val > maxSoFar)
            {
                result.add(node.val);
            }
            return;
        }

        dfs(node.left, result, Math.max(maxSoFar, node.val));
        dfs(node.right, result, Math.max(maxSoFar, node.val));
    }

    private boolean isLeaf(TreeNode node)
    {
        return node.left == null && node.right == null;
    }


    // ---- self-test -------------------------------------------------------
    public static void main(String[] args) {
        TreeLeavesMaxAncestor s = new TreeLeavesMaxAncestor();

        // Example: expect [6, 9]
        TreeNode r = new TreeNode(5);
        r.left = new TreeNode(3);
        r.right = new TreeNode(8);
        r.left.left = new TreeNode(6);
        r.left.right = new TreeNode(4);
        r.right.right = new TreeNode(9);
        check("example", s.findSpecialLeaves(r), Arrays.asList(6, 9));

        // Skewed increasing: 1->2->3, leaf 3, ancestors {1,2}, 3>2 -> [3]
        TreeNode sk = new TreeNode(1);
        sk.right = new TreeNode(2);
        sk.right.right = new TreeNode(3);
        check("skewed-increasing", s.findSpecialLeaves(sk), Arrays.asList(3));

        // Skewed decreasing: 3->2->1, leaf 1, ancestors {3,2}, 1<3 -> []
        TreeNode dk = new TreeNode(3);
        dk.right = new TreeNode(2);
        dk.right.right = new TreeNode(1);
        check("skewed-decreasing", s.findSpecialLeaves(dk), Collections.emptyList());

        // Single node.
        check("single-node", s.findSpecialLeaves(new TreeNode(7)), Collections.emptyList());

        // Empty tree.
        check("empty", s.findSpecialLeaves(null), Collections.emptyList());

        System.out.println("all passed");
    }

    private static void check(String name, List<Integer> got, List<Integer> want) {
        if (!got.equals(want)) {
            throw new AssertionError("FAIL " + name + ": got " + got + " want " + want);
        }
        System.out.println("pass " + name + " -> " + got);
    }
}
