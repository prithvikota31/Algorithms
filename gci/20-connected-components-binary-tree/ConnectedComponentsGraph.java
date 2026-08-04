import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * ============================================================================
 * Problem 20 (Google L4 prep) - FOLLOW-UP 5: Graph version (cycles allowed).
 * ============================================================================
 *
 * PROMPT
 *   The input is no longer a binary tree -- it's a general graph. Each node
 *   has a list of neighbors (any count, cycles allowed). Count the number
 *   of connected components made up of `1`-valued nodes.
 *
 * EXAMPLE
 *   Triangle {a=1,b=1,c=1} all connected to each other, c also connected
 *   to d=0 (ignored), plus an isolated e=1.
 *   -> 2 components: {a,b,c} and {e}.
 *
 * INTUITION
 *   Same "scan + DFS from unvisited 1-node" pattern as the tree version.
 *   The only real addition is a `visited` set: a tree has no cycles so
 *   revisiting a node was never possible, but a graph can have cycles
 *   (or just multiple paths between two nodes), so DFS must mark nodes
 *   visited BEFORE recursing into neighbors, or it loops forever.
 *
 * ALGORITHM
 *   1. Scan every node in the input list. Skip 0-valued or already-visited
 *      nodes.
 *   2. On finding an unvisited 1-node, that's a brand new component:
 *      increment the count and DFS from it.
 *   3. DFS marks the current node visited immediately, then recurses only
 *      into neighbors that are both 1-valued and not yet visited -- this
 *      guard is what makes cycles safe.
 *
 * COMPLEXITY
 *   Time O(V + E): every node and edge is examined at most once.
 *   Space O(V): the visited set (plus O(V) recursion stack worst case).
 * ============================================================================
 */
public class ConnectedComponentsGraph {

    static class Node {
        int val;
        List<Node> neighbors;

        Node(int val) {
            this.val = val;
            this.neighbors = new ArrayList<>();
        }
    }

    public int countComponents(List<Node> nodes) {
        Set<Node> visited = new HashSet<>();
        int componentCount = 0;

        for (Node node : nodes) {
            if (node.val == 1 && !visited.contains(node)) {
                componentCount++;
                dfs(node, visited);
            }
        }

        return componentCount;
    }

    // Marks node visited, then explores only unvisited 1-valued neighbors --
    // the visited check is what prevents infinite loops on cycles.
    private void dfs(Node node, Set<Node> visited) {
        visited.add(node);

        for (Node neighbor : node.neighbors) {
            if (neighbor.val == 1 && !visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }

    private static void connect(Node a, Node b) {
        a.neighbors.add(b);
        b.neighbors.add(a);
    }

    public static void main(String[] args) {
        ConnectedComponentsGraph solution = new ConnectedComponentsGraph();

        // Triangle {a,b,c} (cycle) + d=0 attached to c (ignored) + isolated e.
        Node a = new Node(1);
        Node b = new Node(1);
        Node c = new Node(1);
        Node d = new Node(0);
        Node e = new Node(1);
        connect(a, b);
        connect(b, c);
        connect(c, a);
        connect(c, d);
        check("triangle cycle + isolated node", solution.countComponents(Arrays.asList(a, b, c, d, e)), 2);

        // Straight line, no cycle: still one component.
        Node x = new Node(1);
        Node y = new Node(1);
        Node z = new Node(1);
        connect(x, y);
        connect(y, z);
        check("line graph, no cycle", solution.countComponents(Arrays.asList(x, y, z)), 1);

        // Self-loop must not cause infinite recursion.
        Node self = new Node(1);
        self.neighbors.add(self);
        check("self-loop", solution.countComponents(Arrays.asList(self)), 1);

        // All zeros: no components.
        Node z1 = new Node(0);
        Node z2 = new Node(0);
        connect(z1, z2);
        check("all zeros", solution.countComponents(Arrays.asList(z1, z2)), 0);

        check("empty list", solution.countComponents(new ArrayList<>()), 0);

        System.out.println("all passed");
    }

    private static void check(String name, int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual
                    + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
