/*
 * ============================================================================
 * Problem 10 (Google L4 prep) — Character Order from Pairwise Relationships
 * ============================================================================
 *
 * PROMPT
 * ------
 * You are given a set of pairwise "greater-than" relationships between single
 * characters, e.g.  a > b,  b > c.  Reconstruct ONE valid total ordering of all
 * characters that is consistent with every relationship, or report that it is
 * impossible (the constraints contradict each other, i.e. they form a cycle).
 *
 *   Input : List of pairs. Each pair {x, y} means  x > y  (x comes BEFORE y).
 *   Output: A String listing every distinct character from greatest to least.
 *           Return "" (empty string) if no valid ordering exists.
 *
 * EXAMPLES
 * --------
 *   Input : [[a,b],[b,c]]        Output: "abc"    (a > b > c)
 *   Input : [[a,b],[b,c],[c,a]]  Output: ""       (cycle: a>b>c>a)
 *   Input : [[b,a],[d,c]]        Output: "bdac"   (any order consistent with
 *                                                  b>a and d>c is valid, e.g.
 *                                                  "bdac", "dbac", ...)
 *
 * FOLLOW-UPS THIS HANDLES
 * -----------------------
 *   - Detect impossibility (cycle detection).
 *   - Disconnected characters / multiple independent constraints.
 *   - Return ANY one valid ordering when several exist.
 *
 * ----------------------------------------------------------------------------
 * INTUITION
 * ----------------------------------------------------------------------------
 * "x > y" is a DIRECTED EDGE x -> y ("x must appear before y").  A valid global
 * order that respects all edges is exactly a TOPOLOGICAL SORT of this graph.
 *   - A topological order exists  <=>  the graph is a DAG (no cycle).
 *   - Kahn's algorithm (BFS on in-degrees) both PRODUCES the order and DETECTS
 *     a cycle for free: if we cannot emit every node, a cycle remains.
 *
 * WHY KAHN (BFS) HERE
 *   - Natural cycle detection: processed count < node count  =>  cycle.
 *   - Easy to reason about; no recursion depth limits.
 *
 * ALGORITHM (Kahn's topological sort)
 *   1. Build adjacency list + in-degree map over the DISTINCT characters seen.
 *   2. Seed a queue with every character whose in-degree == 0 (no one is
 *      greater than it — these are the "largest" front-runners).
 *   3. Pop a node, append it to the result, and decrement each neighbor's
 *      in-degree; push neighbors that reach in-degree 0.
 *   4. If the result length == number of distinct characters -> valid order.
 *      Otherwise a cycle exists -> return "".
 *
 * COMPLEXITY
 *   Let V = distinct characters, E = number of pairs.
 *   Time : O(V + E)   Space : O(V + E)
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class CharacterOrderFromPairs {

    /**
     * @param pairs list of relationships; pair {x, y} means x > y (x before y)
     * @return one valid ordering (greatest -> least), or "" if impossible
     */
    public String reconstructOrder(char[][] pairs) {
        // adjacency list: node -> nodes that must come AFTER it
        Map<Character, List<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        // 1. Register every distinct character (isolated chars matter too).
        for (char[] p : pairs) {
            graph.putIfAbsent(p[0], new ArrayList<>());
            graph.putIfAbsent(p[1], new ArrayList<>());
            inDegree.putIfAbsent(p[0], 0);
            inDegree.putIfAbsent(p[1], 0);
        }

        // 1b. Build edges x -> y and count in-degrees.
        //     Skip duplicate pairs so in-degree isn't inflated.
        Set<String> seen = new HashSet<>();
        for (char[] p : pairs) {
            String key = "" + p[0] + p[1];
            if (seen.contains(key)) continue;   // duplicate edge, skip
            seen.add(key);
            graph.get(p[0]).add(p[1]);
            inDegree.put(p[1], inDegree.get(p[1])    + 1);
        }

        // 2. Seed queue with all in-degree-0 nodes (the "greatest" ones).
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) {
                queue.add(c);
            }
        }

        // 3. BFS emit.
        StringBuilder order = new StringBuilder();
        while (!queue.isEmpty()) {
            char node = queue.poll();
            order.append(node);
            for (char next : graph.get(node)) {
                inDegree.put(next, inDegree.get(next) - 1);
                if (inDegree.get(next) == 0) {
                    queue.add(next);
                }
            }
        }

        // 4. All emitted? valid order : cycle.
        return order.length() == graph.size() ? order.toString() : "";
    }

    // ---------------------------------------------------------------------
    // Quick self-test.
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        CharacterOrderFromPairs sol = new CharacterOrderFromPairs();

        System.out.println(sol.reconstructOrder(
                new char[][]{{'a', 'b'}, {'b', 'c'}}));            // abc
        System.out.println(sol.reconstructOrder(
                new char[][]{{'a', 'b'}, {'b', 'c'}, {'c', 'a'}})  // "" (cycle)
                .isEmpty() ? "(impossible)" : "unexpected");
        System.out.println(sol.reconstructOrder(
                new char[][]{{'b', 'a'}, {'d', 'c'}}));            // e.g. bdac
    }
}
