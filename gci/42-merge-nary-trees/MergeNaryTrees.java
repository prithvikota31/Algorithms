import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================================
 * Problem 42 (Google L4 prep) - Merge Two N-ary Trees by Child Name
 * ============================================================================
 *
 * PROMPT
 * ------
 * Merge two N-ary trees. Sibling nodes with the same name represent the same
 * logical node, so recursively combine their subtrees. Keep nodes that occur
 * in only one tree. For matching nodes, tree2's value wins.
 *
 * EXAMPLES
 *   A(1){B(2){X(3)},C(4)} + A(9){B(8){Y(7)},D(6)}
 *     -> A(9){B(8){X(3),Y(7)},C(4),D(6)}
 *   A(1) + A(5) -> A(5)
 *   null + A(1){B(2)} -> A(1){B(2)}
 *
 * INTUITION
 * ---------
 * Once two matching nodes are paired, only their children remain to merge.
 * Index tree1's children by name, then walk tree2's children: a new name is
 * added, while a collision is the same merge problem one level lower.
 *
 * INVARIANT
 * After processing both child lists, childByName contains exactly one merged
 * child for every unique child name present under either input node.
 *
 * ALGORITHM
 * ---------
 * 1. Return the other subtree if either input is null.
 * 2. Create the merged node using tree1's name and tree2's value.
 * 3. Put tree1's children into a LinkedHashMap keyed by name.
 * 4. For each tree2 child, add it if absent; otherwise recursively merge it
 *    with the matching tree1 child and replace that map entry.
 * 5. Copy the map values into the merged node's child list.
 *
 * COMPLEXITY
 * ----------
 * Let N be the total number of nodes across both trees.
 * Time  O(N) expected
 * Space O(N) for the result/maps and O(H) recursion depth
 * ============================================================================
 */
public class MergeNaryTrees {

    public static class TreeNode {
        String name;
        int value;
        List<TreeNode> children;

        TreeNode(String name, int value) {
            this.name = name;
            this.value = value;
            this.children = new ArrayList<>();
        }
    }

    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        // If one tree is missing, there is nothing to merge on that side.
        if (root1 == null) {
            return root2;
        }

        if (root2 == null) {
            return root1;
        }

        // This method merges two versions of the same logical node.
        // Recursive calls below guarantee this by pairing children by name.
        if (!root1.name.equals(root2.name)) {
            throw new IllegalArgumentException(
                    "Cannot merge nodes with different names: "
                            + root1.name + " and " + root2.name);
        }

        // The nodes match, so create one result node. When values conflict,
        // the problem says that the value from tree2 wins.
        TreeNode merged = new TreeNode(root1.name, root2.value);
        Map<String, TreeNode> mergedChildrenByName = new LinkedHashMap<>();

        // Start with every child from tree1. LinkedHashMap preserves their
        // original order while also letting us find a child by name in O(1).
        for (TreeNode child : root1.children) {
            mergedChildrenByName.put(child.name, child);
        }

        // Merge in tree2's children:
        //   - a new name belongs only to tree2, so keep that subtree
        //   - a matching name occurs in both trees, so merge it recursively
        for (TreeNode childFromTree2 : root2.children) {
            TreeNode matchingChildFromTree1 =
                    mergedChildrenByName.get(childFromTree2.name);

            if (matchingChildFromTree1 == null) {
                mergedChildrenByName.put(
                        childFromTree2.name, childFromTree2);
            } else {
                TreeNode mergedChild = mergeTrees(
                        matchingChildFromTree1, childFromTree2);
                mergedChildrenByName.put(childFromTree2.name, mergedChild);
            }
        }

        // The map now contains exactly one child for every distinct name
        // found under either input node.
        merged.children.addAll(mergedChildrenByName.values());
        return merged;
    }

    public static void main(String[] args) {
        MergeNaryTrees solution = new MergeNaryTrees();

        check("recursive merge and tree2 value wins",
                solution.mergeTrees(
                        node("A", 1,
                                node("B", 2, node("X", 3)),
                                node("C", 4)),
                        node("A", 9,
                                node("B", 8, node("Y", 7)),
                                node("D", 6))),
                "A(9){B(8){X(3),Y(7)},C(4),D(6)}");

        check("matching leaves use tree2 value",
                solution.mergeTrees(node("A", 1), node("A", 5)),
                "A(5)");

        check("left subtree missing",
                solution.mergeTrees(null, node("A", 1, node("B", 2))),
                "A(1){B(2)}");

        check("right subtree missing",
                solution.mergeTrees(node("A", 1, node("B", 2)), null),
                "A(1){B(2)}");

        check("tree1 order remains stable",
                solution.mergeTrees(
                        node("A", 1, node("C", 3), node("B", 2)),
                        node("A", 9, node("B", 8), node("D", 4))),
                "A(9){C(3),B(8),D(4)}");

        System.out.println("all passed");
    }

    private static TreeNode node(String name, int value, TreeNode... children) {
        TreeNode node = new TreeNode(name, value);
        Collections.addAll(node.children, children);
        return node;
    }

    private static String serialize(TreeNode node) {
        if (node == null) {
            return "null";
        }

        StringBuilder result = new StringBuilder()
                .append(node.name)
                .append('(')
                .append(node.value)
                .append(')');

        if (!node.children.isEmpty()) {
            result.append('{');

            for (int i = 0; i < node.children.size(); i++) {
                if (i > 0) {
                    result.append(',');
                }
                result.append(serialize(node.children.get(i)));
            }

            result.append('}');
        }

        return result.toString();
    }

    private static void check(String testName, TreeNode actual, String expected) {
        String serialized = serialize(actual);

        if (!serialized.equals(expected)) {
            throw new AssertionError(
                    "FAIL " + testName + ": got " + serialized + " want " + expected);
        }

        System.out.println("pass " + testName + " -> " + serialized);
    }
}
