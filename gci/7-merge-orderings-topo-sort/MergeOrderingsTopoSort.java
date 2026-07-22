/*
 * ============================================================================
 * Problem 7 (Google L4 prep) — Merge Multiple Orderings into One Global Order
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given several preference/dependency lists, construct ONE global ordering that
 * respects every list. Return an empty list if the constraints contradict each
 * other (a cycle). Any one valid order is acceptable (need not be unique).
 *
 *   [A, B, D] , [A, C] , [C, D]
 *   constraints A->B->D and A->C->D  =>  e.g. [A, B, C, D] (or [A, C, B, D]).
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: directed graph + TOPOLOGICAL SORT (Kahn / BFS on indegrees).
 * Each ADJACENT pair in a list is an edge: [A,B,D] -> A->B and B->D.
 * indegree[X] = number of prerequisites of X still unmet; a node is safe to
 * output only when its indegree hits 0. If fewer than N nodes come out, the
 * remaining nodes are trapped in/behind a cycle -> no valid order.
 *
 * Use a Set for adjacency so a duplicate edge (same pair in two lists) does not
 * inflate indegree twice.
 *
 * APPROACHES
 *   Brute force : try every permutation, check all lists -> O(N! * E).
 *   Optimal     : Kahn's algorithm.  Time O(V + E)   Space O(V + E).
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class MergeOrderingsTopoSort {

    // Build one global order respecting every input ordering; empty list on cycle.
    public List<String> buildGlobalOrder(List<List<String>> orderings) {

        // Adjacency as a Set to avoid double-counting duplicate edges.
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        // Register every item first, including items that have no edges.
        for (List<String> ordering : orderings) {
            for (String item : ordering) {
                graph.putIfAbsent(item, new HashSet<>());
                indegree.putIfAbsent(item, 0);
            }
        }

        // Each adjacent pair is a direct ordering constraint before -> after.
        for (List<String> ordering : orderings) {
            for (int i = 0; i < ordering.size() - 1; i++) {
                String before = ordering.get(i);
                String after = ordering.get(i + 1);
                // Bump indegree only when this edge is genuinely new.
                if (graph.get(before).add(after)) {
                    indegree.put(after, indegree.get(after) + 1);
                }
            }
        }

        // Start from every item with no unmet prerequisites.
        Queue<String> available = new ArrayDeque<>();
        for (String item : indegree.keySet()) {
            if (indegree.get(item) == 0) {
                available.offer(item);
            }
        }

        List<String> globalOrder = new ArrayList<>();
        while (!available.isEmpty()) {
            String current = available.poll();
            globalOrder.add(current);

            // Removing current satisfies one prerequisite for each neighbor.
            for (String next : graph.get(current)) {
                indegree.put(next, indegree.get(next) - 1);
                if (indegree.get(next) == 0) {
                    available.offer(next);
                }
            }
        }

        // Some node never reached indegree 0 -> a cycle exists.
        if (globalOrder.size() != indegree.size()) {
            return new ArrayList<>();
        }
        return globalOrder;
    }

    // ------------------------------------------------------------------
    // Quick self-test.  (Topo order is not unique; any valid one is fine.)
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        MergeOrderingsTopoSort sol = new MergeOrderingsTopoSort();

        // 1) Example: A before B/C, both before D -> e.g. [A, B, C, D].
        List<List<String>> in1 = List.of(
            List.of("A", "B", "D"),
            List.of("A", "C"),
            List.of("C", "D")
        );
        System.out.println(sol.buildGlobalOrder(in1)); // a valid order, e.g. [A, B, C, D]

        // 2) Contradiction A->B and B->A -> cycle -> [].
        List<List<String>> in2 = List.of(
            List.of("A", "B"),
            List.of("B", "A")
        );
        System.out.println(sol.buildGlobalOrder(in2)); // []

        // 3) Single chain X->Y->Z -> [X, Y, Z].
        List<List<String>> in3 = List.of(List.of("X", "Y", "Z"));
        System.out.println(sol.buildGlobalOrder(in3)); // [X, Y, Z]

        // 4) Longer 3-way cycle P->Q->R->P -> [].
        List<List<String>> in4 = List.of(
            List.of("P", "Q"),
            List.of("Q", "R"),
            List.of("R", "P")
        );
        System.out.println(sol.buildGlobalOrder(in4)); // []
    }
}
