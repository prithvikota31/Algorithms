import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * ============================================================================
 * Problem 43 (Google L4 prep) - Remove N-ary Tree Leaves in Rounds
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given an N-ary tree, repeatedly remove all current leaves simultaneously
 * until the tree is empty. Return the node values removed in each round.
 *
 * EXAMPLES
 *          1
 *        / | \
 *       2  3  4       -> [[5, 6, 3, 4], [2], [1]]
 *      / \
 *     5   6
 *
 *   1 -> 2 -> 3       -> [[3], [2], [1]]
 *   null              -> []
 *
 * INTUITION
 * ---------
 * Do not repeatedly mutate and rescan the tree. A node's height measured from
 * the bottom is exactly its zero-based removal round: leaves have height 0,
 * and every parent disappears one round after its slowest child. Postorder DFS
 * computes this value because all child rounds are known before the parent.
 *
 * INVARIANT
 * ----------
 * When dfs(node) returns, every node in that subtree has been placed into its
 * correct removal bucket, and the return value is node's removal round.
 *
 * ALGORITHM
 * ---------
 * 1. Run a postorder DFS from the root.
 * 2. Set each node's round to 1 + the maximum child round, or 0 for a leaf.
 * 3. Create that round's bucket when first encountered.
 * 4. Add the node's value to its bucket and return its round to its parent.
 *
 * COMPLEXITY
 * ----------
 * Let N be the number of nodes and H be the tree height.
 * Time  O(N)
 * Space O(H) recursion stack, excluding the O(N) returned output
 * ============================================================================
 */
public class NaryTreeLeafRemoval {

    public static class Node {
        int value;
        List<Node> children;

        Node(int value) {
            this.value = value;
            this.children = new ArrayList<>();
        }
    }

    public List<List<Integer>> findLeaves(Node root) {
        // Index i stores all nodes removed during round i.
        List<List<Integer>> removalRounds = new ArrayList<>();

        if (root != null) {
            findRemovalRound(root, removalRounds);
        }

        return removalRounds;
    }

    private int findRemovalRound(
            Node node,
            List<List<Integer>> removalRounds) {

        // A leaf has no children, so its removal round remains 0.
        // A non-leaf is removed one round after its last surviving child.
        int round = 0;

        for (Node child : node.children) {
            int childRound = findRemovalRound(child, removalRounds);
            round = Math.max(
                    round,
                    childRound + 1);
        }

        // Postorder traversal discovers rounds in order: before a node can be
        // assigned to round r, one of its descendants has already created all
        // buckets from 0 through r - 1.
        if (removalRounds.size() == round) {
            removalRounds.add(new ArrayList<>());
        }

        // Place this node into its computed round, then tell its parent when
        // this subtree's root disappears.
        removalRounds.get(round).add(node.value);
        return round;
    }

    public static void main(String[] args) {
        NaryTreeLeafRemoval solution = new NaryTreeLeafRemoval();

        check("sample",
                solution.findLeaves(
                        node(1,
                                node(2, node(5), node(6)),
                                node(3),
                                node(4))),
                "[[5, 6, 3, 4], [2], [1]]");

        check("skewed chain",
            solution.findLeaves(node(1, node(2, node(3)))),
            "[[3], [2], [1]]");

        check("uneven branches",
                solution.findLeaves(
                        node(1,
                                node(2, node(5, node(7))),
                                node(3, node(6)),
                                node(4))),
                "[[7, 6, 4], [5, 3], [2], [1]]");

        check("single node", solution.findLeaves(node(9)), "[[9]]");
        check("null tree", solution.findLeaves(null), "[]");

        System.out.println("all passed");
    }

    private static Node node(int value, Node... children) {
        Node node = new Node(value);
        Collections.addAll(node.children, children);
        return node;
    }

    private static void check(
            String testName,
            List<List<Integer>> actual,
            String expected) {

        String rendered = actual.toString();

        if (!rendered.equals(expected)) {
            throw new AssertionError(
                    "FAIL " + testName + ": got " + rendered + " want " + expected);
        }

        System.out.println("pass " + testName + " -> " + rendered);
    }
}
