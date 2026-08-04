import java.util.*;

/*
 * Problem 19 — Follow-up 1: return the full root-to-leaf PATHS (not just leaf values)
 * for every leaf that is greater than all its ancestors.
 *
 * EXAMPLE:
 *           5
 *          / \
 *         3   8
 *        /     \
 *       6       9
 *   output: [[5, 3, 6], [5, 8, 9]]
 *
 * WHAT CHANGES vs BASE:
 *   Base carried only a SUMMARY of the path (maxAncestor) because the leaf only
 *   asked "am I valid?". Now we also need "HOW did I get here?", so DFS carries a
 *   second piece of downward state: the live `currentPath`, maintained as a stack —
 *   push on entry, POP on exit (backtracking). At a qualifying leaf, snapshot a
 *   COPY of the path (the live list keeps mutating).
 *
 * SEEDING NOTE: seeded with Integer.MIN_VALUE (vacuous-truth) — a lone leaf has no
 *   ancestors so it qualifies. (Base file seeded with root.val, so single node -> []
 *   there; here single node -> [[val]].)
 *
 * TAKEAWAY: tree asks for "the path" -> carry a path list + backtrack.
 *           tree asks for a property -> carry only the summary (max/sum/count).
 *
 * COMPLEXITY: O(N) time, O(H) space (recursion + path).
 */
public class TreeLeafPaths {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    public List<List<Integer>> findPaths(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        dfs(root, Integer.MIN_VALUE, new ArrayList<>(), result);
        return result;
    }

    private void dfs(TreeNode node, int maxAncestor,
                     List<Integer> currentPath, List<List<Integer>> result) {
        if (node == null) return;

        currentPath.add(node.val);
        int currentMax = Math.max(maxAncestor, node.val);

        if (node.left == null && node.right == null) {
            if (node.val > maxAncestor) {
                result.add(new ArrayList<>(currentPath)); // copy, not the live list
            }
        }

        dfs(node.left, currentMax, currentPath, result);
        dfs(node.right, currentMax, currentPath, result);

        currentPath.remove(currentPath.size() - 1); // backtrack
    }

    // ---- self-test -------------------------------------------------------
    public static void main(String[] args) {
        TreeLeafPaths s = new TreeLeafPaths();

        // Example: expect [[5,3,6],[5,8,9]]
        TreeNode r = new TreeNode(5);
        r.left = new TreeNode(3);
        r.right = new TreeNode(8);
        r.left.left = new TreeNode(6);
        r.right.right = new TreeNode(9);
        check("example", s.findPaths(r),
                Arrays.asList(Arrays.asList(5, 3, 6), Arrays.asList(5, 8, 9)));

        // Skewed increasing 1->2->3: leaf 3 > {1,2} -> [[1,2,3]]
        TreeNode sk = new TreeNode(1);
        sk.right = new TreeNode(2);
        sk.right.right = new TreeNode(3);
        check("skewed-increasing", s.findPaths(sk),
                Arrays.asList(Arrays.asList(1, 2, 3)));

        // Skewed decreasing 3->2->1: leaf 1 < max ancestor 3 -> []
        TreeNode dk = new TreeNode(3);
        dk.right = new TreeNode(2);
        dk.right.right = new TreeNode(1);
        check("skewed-decreasing", s.findPaths(dk), Collections.emptyList());

        // Single node: qualifies here (MIN_VALUE seed) -> [[7]]
        check("single-node", s.findPaths(new TreeNode(7)),
                Arrays.asList(Arrays.asList(7)));

        // Mixed: branch where the failing leaf is dropped but path is not corrupted.
        //        5
        //       / \
        //      3   8
        //     /     \
        //    6       4   (4 < 8, dropped)  -> only [5,3,6]
        TreeNode m = new TreeNode(5);
        m.left = new TreeNode(3);
        m.right = new TreeNode(8);
        m.left.left = new TreeNode(6);
        m.right.right = new TreeNode(4);
        check("drop-and-restore", s.findPaths(m),
                Arrays.asList(Arrays.asList(5, 3, 6)));

        // Empty.
        check("empty", s.findPaths(null), Collections.emptyList());

        System.out.println("all passed");
    }

    private static void check(String name, List<List<Integer>> got, List<List<Integer>> want) {
        if (!got.equals(want)) {
            throw new AssertionError("FAIL " + name + ": got " + got + " want " + want);
        }
        System.out.println("pass " + name + " -> " + got);
    }
}
