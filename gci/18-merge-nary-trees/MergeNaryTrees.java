/*
 * ============================================================================
 * Problem 18 (Google L4 prep) — Merge Two N-ary Trees by Matching Child Names
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given two hierarchical trees whose nodes have a NAME and a VALUE, merge them:
 *   - Nodes with the same name (at the same position) are combined; the
 *     value-conflict rule here is ADD the two values.
 *   - Children are matched BY NAME (unique among siblings) and merged
 *     recursively.
 *   - A child present in only one tree is kept as-is.
 *   - If one whole subtree is missing (null), keep the other.
 *
 * EXAMPLE
 *   Tree 1:  A(10) -> { B(5), C(7) }
 *   Tree 2:  A(20) -> { B(3), D(9) }
 *   Merged:  A(30) -> { B(8), C(7), D(9) }
 *
 * ----------------------------------------------------------------------------
 * Fill in mergeTrees(). The main() self-test below is ready to run:
 *     javac MergeNaryTrees.java && java -ea MergeNaryTrees
 * The harness compares trees by a canonical serialization that sorts children
 * by name, so the ORDER you attach children in does not matter.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeNaryTrees {

    // A tree node: a name (unique among its siblings), a value, and its children.
    static class Node{
        String name;
        int value;
        List<Node> children;

        public Node(String name, int value)
        {
            this.name = name;
            this.value = value;
            this.children = new ArrayList<>();   // never null, so callers can always add
        }
    }

    /*
     * MENTAL MAP: tree DFS + HashMap.
     * Merging two nodes with the same name = combine their values, then merge
     * their children matched BY NAME. A HashMap of root1's children gives O(1)
     * "does root2's child already exist?" lookups, turning an O(children1 *
     * children2) scan into O(total nodes).
     *
     * Value-conflict rule here: ADD the two values.
     * Time O(N)  Space O(N), N = total nodes across both trees.
     */
    public Node mergeTrees(Node root1, Node root2) {
        // If one subtree is missing, the merge is just the other subtree.
        if(root1 == null)   return root2;
        if(root2 == null)   return root1;

        // Same node: combine values (conflict rule = sum).
        Node mergedNode = new Node(root1.name, root1.value + root2.value);

        // Index root1's children by name for O(1) matching against root2's children.
        Map<String, Node> map = new HashMap<>();
        for(Node node1: root1.children)
        {
            map.put(node1.name, node1);
        }

        // Walk root2's children: matched name -> merge recursively; new name -> attach.
        for(Node node2: root2.children)
        {
            if(map.containsKey(node2.name))
            {
                Node mergedChildNode = mergeTrees(map.get(node2.name), node2);
                mergedNode.children.add(mergedChildNode);
                // Remove so it isn't re-added in the leftover pass below.
                map.remove(node2.name);
            }
            else
            {
                mergedNode.children.add(node2);
            }
        }

        // Whatever remains in the map existed ONLY in root1 -> attach unchanged.
        for(Node node1: map.values())
        {
            mergedNode.children.add(node1);
        }

        return mergedNode;
    }

    // ------------------------------------------------------------------------
    // Self-test harness. Run with `java -ea MergeNaryTrees`.
    // ------------------------------------------------------------------------
    private static int passed = 0;
    private static int failed = 0;

    // Build a node with children inline: node("A", 10, node("B", 5), ...)
    private static Node node(String name, int value, Node... kids) {
        Node n = new Node(name, value);
        for (Node k : kids) {
            n.children.add(k);
        }
        return n;
    }

    // Canonical form: children sorted by name, so attach order is irrelevant.
    private static String serialize(Node n) {
        if (n == null) {
            return "null";
        }
        List<Node> kids = new ArrayList<>(n.children);
        kids.sort((a, b) -> a.name.compareTo(b.name));
        StringBuilder sb = new StringBuilder();
        sb.append(n.name).append('(').append(n.value).append("){");
        for (int i = 0; i < kids.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(serialize(kids.get(i)));
        }
        return sb.append('}').toString();
    }

    private static void check(String name, Node expected, Node a, Node b) {
        Node actual;
        try {
            actual = new MergeNaryTrees().mergeTrees(a, b);
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL " + name + " -> threw " + e);
            return;
        }
        String exp = serialize(expected);
        String got = serialize(actual);
        if (exp.equals(got)) {
            passed++;
            System.out.println("pass " + name);
        } else {
            failed++;
            System.out.println("FAIL " + name + "\n     expected " + exp + "\n     got      " + got);
        }
    }

    public static void main(String[] args) {
        // Base example: shared A and B (summed), plus one unique child each side.
        check("example",
            node("A", 30, node("B", 8), node("C", 7), node("D", 9)),
            node("A", 10, node("B", 5), node("C", 7)),
            node("A", 20, node("B", 3), node("D", 9)));

        // One whole tree missing -> keep the other.
        check("left-null",
            node("A", 1, node("B", 2)),
            null,
            node("A", 1, node("B", 2)));
        check("right-null",
            node("A", 1, node("B", 2)),
            node("A", 1, node("B", 2)),
            null);

        // Fully disjoint children under a shared root.
        check("disjoint-children",
            node("root", 2, node("x", 1), node("y", 1)),
            node("root", 1, node("x", 1)),
            node("root", 1, node("y", 1)));

        // Deep recursive merge: same path /home/docs on both sides.
        check("deep-merge",
            node("home", 0,
                node("docs", 0, node("a.txt", 3)),
                node("photos", 0, node("img.png", 9))),
            node("home", 0, node("docs", 0, node("a.txt", 1))),
            node("home", 0,
                node("docs", 0, node("a.txt", 2)),
                node("photos", 0, node("img.png", 9))));

        // Identical structure -> every value summed.
        check("identical-sum",
            node("A", 4, node("B", 6, node("C", 8))),
            node("A", 2, node("B", 3, node("C", 4))),
            node("A", 2, node("B", 3, node("C", 4))));

        // Single nodes, no children.
        check("leaves-only",
            node("A", 5),
            node("A", 2),
            node("A", 3));

        System.out.println("----");
        System.out.println("passed=" + passed + " failed=" + failed);
        assert failed == 0 : failed + " test(s) failed";
    }
}
