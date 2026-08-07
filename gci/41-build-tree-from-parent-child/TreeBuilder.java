import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * ============================================================================
 * Problem 41 (Google L4 prep) — Build a Tree From Parent-Child Relationships
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given pairs [parent, child], construct the N-ary tree they describe and
 * return its root with every `children` list populated. Pairs arrive in
 * ARBITRARY order — a child can be named before its own parent ever appears.
 *
 * ASSUMPTIONS TO CONFIRM WITH THE INTERVIEWER
 *   N-ary (not binary), every child has exactly one parent, exactly one root,
 *   node values unique. If any of those is not guaranteed, see FOLLOW-UPS —
 *   the code below silently misbehaves rather than reporting bad input.
 *
 * EXAMPLES
 *   [[A,B],[A,C],[B,D],[B,E]]          [[B,D],[A,B]]
 *           A                              A
 *          / \                             |
 *         B   C                            B
 *        / \                               |
 *       D   E                              D
 *   -> root A                          -> root A (child pair seen FIRST)
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * CREATE EVERYBODY IN A MAP, CONNECT EVERYBODY, THEN FIND THE PERSON WHO WAS
 * NEVER A CHILD.
 *
 * The whole difficulty is out-of-order input. Do NOT try to build a node only
 * once you have seen its parent — you would have to buffer orphans and stitch
 * them later. Instead keep
 *
 *     nodes[value] = the ONE TreeNode object for that value
 *
 * and `computeIfAbsent` it into existence the first time the value is
 * mentioned, as parent or as child, whichever comes first. Because every
 * mention resolves to the same object, wiring `parent.children.add(child)` is
 * always correct no matter what order the pairs arrive in. That is the trick:
 * identity is owned by the map, not by traversal order.
 *
 * ROOT DETECTION is just indegree 0 expressed as a set: track every value that
 * ever appeared in the CHILD slot; the single value missing from that set is
 * the root. No traversal, no counting.
 *
 * Note this never searches the partially built tree, which is what makes it
 * linear — the brute force "walk the tree to find the parent" is O(N^2).
 *
 * APPROACHES
 *   Brute force : for each pair, traverse the partial tree to locate the
 *                 parent, and park children whose parent is not there yet.
 *                 O(N^2) and fiddly.
 *   Optimal     : value -> node map + child-value set (below). One pass, O(N).
 *
 * FOLLOW-UPS (understand only — deliberately NOT implemented, this code
 * assumes well-formed input)
 *   (1) Invalid input detection:
 *       - multiple parents for one child -> the second `parent.children.add`
 *         silently duplicates the subtree instead of erroring. Guard by
 *         rejecting a childValue already present in `childValues`.
 *       - a CYCLE (A->B, B->A) makes every value a child, so no root exists
 *         and this returns null. Covered by a test below.
 *       - MULTIPLE ROOTS (a forest) is the nastiest: the loop returns whichever
 *         non-child value HashMap iteration happens to reach first, so the
 *         result is non-deterministic. Detect by collecting ALL non-child
 *         values and asserting exactly one.
 *   (2) Specific child ordering: `children` currently follows the order the
 *       pairs were supplied. Sort each list afterwards if the interviewer
 *       wants deterministic ordering by value.
 *
 * COMPLEXITY
 *   Let N = number of relationships (so O(N) distinct values).
 *   Time  O(N)
 *   Space O(N)
 * ----------------------------------------------------------------------------
 */
public class TreeBuilder {

    static class TreeNode {
        String value;
        List<TreeNode> children;

        TreeNode(String value) {
            this.value = value;
            this.children = new ArrayList<>();
        }
    }

    public TreeNode buildTree(String[][] relationships) {

        if (relationships == null || relationships.length == 0) {
            return null;
        }

        // One unique TreeNode object per value.
        Map<String, TreeNode> nodes = new HashMap<>();

        // Any node appearing here cannot be the root.
        Set<String> childValues = new HashSet<>();

        // Build all parent -> child connections.
        for (String[] relationship : relationships) {

            String parentValue = relationship[0];
            String childValue = relationship[1];

            TreeNode parent = nodes.computeIfAbsent(
                parentValue,
                value -> new TreeNode(value)
            );

            TreeNode child = nodes.computeIfAbsent(
                childValue,
                value -> new TreeNode(value)
            );

            parent.children.add(child);
            childValues.add(childValue);
        }

        // The root is the only node that never appeared as a child.
        for (String value : nodes.keySet()) {
            if (!childValues.contains(value)) {
                return nodes.get(value);
            }
        }

        return null;
    }

    public static void main(String[] args) {
        TreeBuilder solution = new TreeBuilder();

        check("standard n-ary tree",
                solution.buildTree(new String[][] {
                        { "A", "B" }, { "A", "C" }, { "B", "D" }, { "B", "E" } }),
                "A(B(D,E),C)");

        // The [B,D] pair names D before A->B is ever seen.
        check("child pair arrives before its parent pair",
                solution.buildTree(new String[][] { { "B", "D" }, { "A", "B" } }),
                "A(B(D))");

        check("single relationship",
                solution.buildTree(new String[][] { { "A", "B" } }),
                "A(B)");

        // children follows the order the pairs were supplied.
        check("child order follows input order",
                solution.buildTree(new String[][] {
                        { "A", "C" }, { "A", "B" }, { "A", "D" } }),
                "A(C,B,D)");

        check("deep chain",
                solution.buildTree(new String[][] {
                        { "C", "D" }, { "A", "B" }, { "B", "C" } }),
                "A(B(C(D)))");

        // Every value is someone's child, so no root exists.
        check("cycle has no root",
                solution.buildTree(new String[][] { { "A", "B" }, { "B", "A" } }),
                "null");

        check("empty input", solution.buildTree(new String[][] {}), "null");
        check("null input", solution.buildTree(null), "null");

        System.out.println("all passed");
    }

    /** value(child1,child2,...) — children in list order. */
    private static String serialize(TreeNode node) {
        if (node == null) {
            return "null";
        }

        if (node.children.isEmpty()) {
            return node.value;
        }

        StringBuilder rendered = new StringBuilder(node.value).append('(');

        for (int i = 0; i < node.children.size(); i++) {
            if (i > 0) {
                rendered.append(',');
            }
            rendered.append(serialize(node.children.get(i)));
        }

        return rendered.append(')').toString();
    }

    private static void check(String name, TreeNode actualRoot, String expected) {
        String actual = serialize(actualRoot);

        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }

        System.out.println("pass " + name + " -> " + actual);
    }
}
