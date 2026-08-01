/*
 * ============================================================================
 * Problem 18 — Follow-up 2: Immutable Merge (no shared references / deep copy)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Same name-matched N-ary merge, but the inputs are READ-ONLY: the result must
 * be a fully independent tree. No node of the merged tree may be the same
 * object as any node in root1 or root2, so mutating an input later never
 * affects the merged result.
 *
 * WHAT CHANGES vs the base merge
 * ------------------------------
 *   1. Name MISS: attach a DEEP CLONE of the subtree, not the subtree itself.
 *   2. One whole subtree missing (root1==null / root2==null): return a CLONE of
 *      the other side, not the node itself (the base version aliased it).
 *   3. Name MATCH: already safe — it builds a brand-new node and recurses.
 *
 * New concept: deep copy / object ownership. Traversal logic is unchanged.
 *
 * Value-conflict rule here: sum (kept simple; orthogonal to copying).
 *
 * ----------------------------------------------------------------------------
 * Run: javac MergeTreesImmutable.java && java -ea MergeTreesImmutable
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MergeTreesImmutable {

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

    // Deep copy: brand-new node objects all the way down.
    private Node clone(Node node) {
        Node copy = new Node(node.name, node.value);
        for (Node child : node.children) {
            copy.children.add(clone(child));
        }
        return copy;
    }

    public Node mergeTrees(Node root1, Node root2) {
        // One side missing: return an independent COPY of the other (not itself).
        if (root1 == null) return root2 == null ? null : clone(root2);
        if (root2 == null) return clone(root1);

        // Matched node: fresh object, values combined.
        Node merged = new Node(root1.name, root1.value + root2.value);

        Map<String, Node> childByName = new HashMap<>();
        for (Node child1 : root1.children) {
            childByName.put(child1.name, child1);
        }

        for (Node child2 : root2.children) {
            Node child1 = childByName.get(child2.name);
            if (child1 != null) {
                // Match -> recurse; recursion returns all-new nodes.
                merged.children.add(mergeTrees(child1, child2));
                childByName.remove(child2.name);
            } else {
                // Only in root2 -> attach a COPY, never the input node.
                merged.children.add(clone(child2));
            }
        }

        // Only in root1 -> attach a COPY.
        for (Node leftover : childByName.values()) {
            merged.children.add(clone(leftover));
        }

        return merged;
    }

    // ------------------------------------------------------------------------
    // Self-test harness. Run with `java -ea MergeTreesImmutable`.
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

    // Collect every node object (by identity) in a tree.
    private static void collect(Node n, Set<Node> into) {
        if (n == null) {
            return;
        }
        into.add(n);
        for (Node c : n.children) {
            collect(c, into);
        }
    }

    private static void pass(String name) { passed++; System.out.println("pass " + name); }
    private static void fail(String name, String why) { failed++; System.out.println("FAIL " + name + " -> " + why); }

    private static void checkValue(String name, Node expected, Node a, Node b) {
        Node merged = new MergeTreesImmutable().mergeTrees(a, b);
        String exp = serialize(expected), got = serialize(merged);
        if (exp.equals(got)) pass(name); else fail(name, "expected " + exp + " got " + got);
    }

    // Verify the merged tree shares ZERO node objects with either input.
    private static void checkIndependent(String name, Node a, Node b) {
        Node merged = new MergeTreesImmutable().mergeTrees(a, b);

        Set<Node> inputs = Collections.newSetFromMap(new IdentityHashMap<>());
        collect(a, inputs);
        collect(b, inputs);

        Set<Node> out = Collections.newSetFromMap(new IdentityHashMap<>());
        collect(merged, out);

        for (Node n : out) {
            if (inputs.contains(n)) {
                fail(name, "merged reused an input node: " + n.name);
                return;
            }
        }
        pass(name);
    }

    public static void main(String[] args) {
        // Correctness of the merge itself.
        checkValue("merge-shape",
            node("A", 20, node("B", 5), node("C", 7)),
            node("A", 10, node("B", 5)),
            node("A", 10, node("C", 7)));

        // No shared references anywhere.
        checkIndependent("independent-basic",
            node("A", 10, node("B", 5)),
            node("A", 10, node("C", 7)));
        checkIndependent("independent-deep",
            node("home", 0, node("docs", 0, node("a.txt", 1))),
            node("home", 0, node("docs", 0, node("a.txt", 2)), node("photos", 0, node("img", 9))));
        // One side null must still be an independent copy, not the original.
        checkIndependent("independent-left-null", null,
            node("A", 1, node("B", 2)));

        // Mutating an input AFTER merge must NOT change the merged tree.
        Node t1 = node("A", 10, node("B", 5));
        Node t2 = node("A", 10, node("C", 7));
        Node merged = new MergeTreesImmutable().mergeTrees(t1, t2);
        String before = serialize(merged);
        t2.children.get(0).value = 1000;   // mutate C in the input
        t1.children.get(0).value = 2000;   // mutate B in the input
        String after = serialize(merged);
        if (before.equals(after)) pass("input-mutation-isolated");
        else fail("input-mutation-isolated", "merged changed: " + before + " -> " + after);

        System.out.println("----");
        System.out.println("passed=" + passed + " failed=" + failed);
        assert failed == 0 : failed + " test(s) failed";
    }
}
