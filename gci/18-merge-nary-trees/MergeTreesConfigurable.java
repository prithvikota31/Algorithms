/*
 * ============================================================================
 * Problem 18 — Follow-up 1: Pluggable Conflict-Resolution Rule
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Same name-matched N-ary merge as the base problem, but the value-conflict
 * rule must be supplied by the CALLER instead of being hard-coded to `+`.
 *
 * API:
 *   Node mergeTrees(Node root1, Node root2, ValueResolver resolver)
 *   interface ValueResolver { int resolve(int value1, int value2); }
 *
 * EXAMPLE  (Tree1: A(10)->X(100),  Tree2: A(20)->X(150))
 *   sum        (a,b)->a+b   -> A(30)->X(250)
 *   second-win (a,b)->b     -> A(20)->X(150)
 *   max        Math::max    -> A(20)->X(150)
 *
 * IDEA UNDER TEST: separate the STABLE traversal (name-match + recurse) from
 * the CONFIGURABLE business rule (how to combine two values). Strategy pattern.
 *
 * ----------------------------------------------------------------------------
 * Fill in mergeTrees(). Run: javac MergeTreesConfigurable.java && java -ea MergeTreesConfigurable
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeTreesConfigurable {

    // Caller-supplied rule for combining two values with the same name.
    interface ValueResolver {
        int resolve(int value1, int value2);
    }

    static class Node {
        String name;
        int value;
        List<Node> children;

        Node(String name, int value) {
            this.name = name;
            this.value = value;
            this.children = new ArrayList<>();
        }
    }

    public Node mergeTrees(Node root1, Node root2, ValueResolver resolver) {
        // If one subtree is missing, the merge is just the other subtree.
        if (root1 == null) return root2;
        if (root2 == null) return root1;

        // Same node: combine values via the CALLER's rule (not a hard-coded +).
        Node merged = new Node(root1.name, resolver.resolve(root1.value, root2.value));

        // Index root1's children by name for O(1) matching.
        Map<String, Node> childByName = new HashMap<>();
        for (Node child1 : root1.children) {
            childByName.put(child1.name, child1);
        }

        // Match root2's children: name hit -> merge recursively (same rule); miss -> attach.
        for (Node child2 : root2.children) {
            Node child1 = childByName.get(child2.name);
            if (child1 != null) {
                merged.children.add(mergeTrees(child1, child2, resolver));
                childByName.remove(child2.name);
            } else {
                merged.children.add(child2);
            }
        }

        // Remaining children existed only in root1 -> attach unchanged.
        for (Node leftover : childByName.values()) {
            merged.children.add(leftover);
        }

        return merged;
    }

    // ------------------------------------------------------------------------
    // Self-test harness. Run with `java -ea MergeTreesConfigurable`.
    // ------------------------------------------------------------------------
    private static int passed = 0;
    private static int failed = 0;

    private static Node node(String name, int value, Node... kids) {
        Node n = new Node(name, value);
        for (Node k : kids) {
            n.children.add(k);
        }
        return n;
    }

    // Canonical form: children sorted by name so attach order is irrelevant.
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

    private static void check(String name, Node expected, Node a, Node b, ValueResolver rule) {
        Node actual;
        try {
            actual = new MergeTreesConfigurable().mergeTrees(a, b, rule);
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL " + name + " -> threw " + e);
            return;
        }
        String exp = serialize(expected), got = serialize(actual);
        if (exp.equals(got)) {
            passed++;
            System.out.println("pass " + name);
        } else {
            failed++;
            System.out.println("FAIL " + name + "\n     expected " + exp + "\n     got      " + got);
        }
    }

    public static void main(String[] args) {
        // Tree1: A(10)->X(100),  Tree2: A(20)->X(150)
        Node t1 = node("A", 10, node("X", 100));
        Node t2 = node("A", 20, node("X", 150));

        check("sum",        node("A", 30, node("X", 250)), t1, t2, (x, y) -> x + y);
        check("second-win", node("A", 20, node("X", 150)), t1, t2, (x, y) -> y);
        check("max",        node("A", 20, node("X", 150)), t1, t2, Math::max);
        check("min",        node("A", 10, node("X", 100)), t1, t2, Math::min);

        // Rule must still apply while structure differs: shared A, one unique child each side.
        check("mixed-children",
            node("A", 3, node("P", 5), node("Q", 7)),
            node("A", 1, node("P", 5)),
            node("A", 2, node("Q", 7)),
            (x, y) -> x + y);

        // Rule must apply at EVERY matched level (deep).
        check("deep",
            node("A", 2, node("B", 6, node("C", 10))),
            node("A", 1, node("B", 3, node("C", 4))),
            node("A", 1, node("B", 3, node("C", 6))),
            (x, y) -> x + y);

        System.out.println("----");
        System.out.println("passed=" + passed + " failed=" + failed);
        assert failed == 0 : failed + " test(s) failed";
    }
}
