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

            for(char nei: graph.getOrDefault(cur, Collections.emptySet()))
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

        // Repeated pair must not inflate in-degree into a false cycle.
        System.out.println(sol.reconstructOrder(
                new char[][]{{'a', 'b'}, {'a', 'b'}}));            // ab
    }
}
