/*
 * ============================================================================
 * Problem 8 (Google L4 prep) — Character Order from Pairwise Relationships
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
        //Each pair {x, y} means  x > y  (x comes BEFORE y).
        // x-> y

        //build a graph
        Map<Character, Set<Character>> graph = new HashMap<>();
        //indegree list
        Map<Character, Integer> inDegree = new HashMap<>();


        for(char[] pair: pairs)
        {
            // add() is false on a repeated pair, so the counter stays in step with the stored edges.
            if(graph.computeIfAbsent(pair[0], k -> new HashSet<>()).add(pair[1]))
            {
                inDegree.put(pair[1], inDegree.getOrDefault(pair[1], 0) + 1);
            }
            graph.putIfAbsent(pair[1], new HashSet<>());
            inDegree.putIfAbsent(pair[0], 0);
        }
        // we got graph and inDegree 

        StringBuilder sb = new StringBuilder();
        Deque<Character> q = new ArrayDeque<>();

        for(Map.Entry<Character, Integer> entry: inDegree.entrySet())
        {
            int value = entry.getValue();
            char ch = entry.getKey();
            if(value == 0)
            {
                q.offer(ch);
            }
        }
        

        while(!q.isEmpty())
        {
            char cur = q.poll();
            sb.append(cur);

            for(char nei: graph.get(cur))
            {
                inDegree.put(nei, inDegree.get(nei) - 1);

                if(inDegree.get(nei) == 0)
                {
                    q.offer(nei);
                }
            }
        }

        if(sb.length() == inDegree.size())
        {
            return sb.toString();
        }
        else{
            return "";
        }
           
    }

    /**
     * Follow-up: determine whether the constraints produce exactly one order.
     *
     * @param pairs list of relationships; pair {x, y} means x before y
     * @return true only when a valid ordering exists and is unique
     */
    public boolean hasUniqueOrder(char[][] pairs) {
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        for (char[] pair : pairs) {
            if (graph.computeIfAbsent(pair[0], key -> new HashSet<>()).add(pair[1])) {
                inDegree.put(pair[1], inDegree.getOrDefault(pair[1], 0) + 1);
            }
            if(!inDegree.containsKey(pair[0]))
            {
                inDegree.put(pair[0], 0);
            }
        }

        Deque<Character> queue = new ArrayDeque<>();
        for (Map.Entry<Character, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        int processed = 0;

        while(!queue.isEmpty())
        {
            if(queue.size() > 1)
            {
                return false;
            }

            char cur = queue.poll();
            processed++;
            if(!graph.containsKey(cur))
            {
                continue;
            }
            for(char nei: graph.get(cur))
            {
                inDegree.put(nei, inDegree.getOrDefault(nei, 0) - 1);

                if(inDegree.get(nei) == 0)
                {
                    queue.offer(nei);
                }
            }
        }

        return processed == inDegree.size();
    }

    // ---------------------------------------------------------------------
    // Quick self-test.
    // ---------------------------------------------------------------------
    private static void verifyUniqueOrder(
            CharacterOrderFromPairs solution,
            String name,
            char[][] pairs,
            boolean expected) {
        boolean actual = solution.hasUniqueOrder(pairs);
        if (actual != expected) {
            throw new AssertionError(
                    name + ": expected " + expected + ", but got " + actual);
        }
        System.out.println("PASS: " + name);
    }

    public static void main(String[] args) {
        CharacterOrderFromPairs sol = new CharacterOrderFromPairs();

        System.out.println(sol.reconstructOrder(
                new char[][]{{'a', 'b'}, {'b', 'c'}}));            // abc
        System.out.println(sol.reconstructOrder(
                new char[][]{{'a', 'b'}, {'b', 'c'}, {'c', 'a'}})  // "" (cycle)
                .isEmpty() ? "(impossible)" : "unexpected");
        System.out.println(sol.reconstructOrder(
                new char[][]{{'b', 'a'}, {'d', 'c'}}));            // e.g. bdac

        // Repeated pair must not inflate in-degree into a false cycle.
        System.out.println(sol.reconstructOrder(
                new char[][]{{'a', 'b'}, {'a', 'b'}}));            // ab

        System.out.println("\nUnique-order follow-up tests:");
        verifyUniqueOrder(sol, "linear chain is unique",
            new char[][]{{'a', 'b'}, {'b', 'c'}}, true);
        verifyUniqueOrder(sol, "single relationship is unique",
            new char[][]{{'a', 'b'}}, true);
        verifyUniqueOrder(sol, "two initial choices are ambiguous",
            new char[][]{{'a', 'c'}, {'b', 'c'}}, false);
        verifyUniqueOrder(sol, "branching choices are ambiguous",
            new char[][]{{'a', 'b'}, {'a', 'c'}}, false);
        verifyUniqueOrder(sol, "disconnected constraints are ambiguous",
            new char[][]{{'a', 'b'}, {'c', 'd'}}, false);
        verifyUniqueOrder(sol, "cycle has no valid order",
            new char[][]{{'a', 'b'}, {'b', 'c'}, {'c', 'a'}}, false);
        verifyUniqueOrder(sol, "self-loop has no valid order",
            new char[][]{{'a', 'a'}}, false);
        verifyUniqueOrder(sol, "duplicate edges preserve uniqueness",
            new char[][]{{'a', 'b'}, {'a', 'b'}, {'b', 'c'}}, true);
        verifyUniqueOrder(sol, "redundant transitive edge remains unique",
            new char[][]{{'a', 'b'}, {'b', 'c'}, {'a', 'c'}}, true);
    }
}
