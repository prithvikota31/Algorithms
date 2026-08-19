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
        List<String> result = new ArrayList<>();
        //A, B, C, D
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        buildGraph(graph, inDegree,  orderings);
        //now we have graph and inDegree
        // with all possible string as nodes in both
        //kahns
        Deque<String> q = new ArrayDeque<>();
        for(Map.Entry<String, Integer> entry: inDegree.entrySet())
        {
            if(entry.getValue() == 0)
            {
                q.offer(entry.getKey());
            }
        }

        while(!q.isEmpty())
        {
            String cur = q.poll();
            result.add(cur);
            //adj nodes
            for(String nei: graph.get(cur))
            {
                inDegree.put(nei, inDegree.get(nei) - 1);
                int neiInDegree = inDegree.get(nei);
                if(neiInDegree == 0)
                {
                    q.offer(nei);
                }
            }
        }
        //verify if we had a cycle
        if(result.size() == inDegree.size())
        {
            return result;
        }
        else
        {
            return new ArrayList<>();
        }

    }

    private void buildGraph(Map<String, List<String>> graph,
        Map<String, Integer> inDegree, List<List<String>> orderings)
    {
        //A, B, C, d
        //orderings.get(i) - list of string
        for(int i = 0; i < orderings.size(); i++)
        {
            List<String> curList = orderings.get(i);

            for (String item : curList) {
                graph.putIfAbsent(item, new ArrayList<>());
                inDegree.putIfAbsent(item, 0);
            }
            for(int j = 0; j < curList.size(); j++)
            {
                if(j == 0)  continue;
                //A, b
                // A-> b
                //x -> y
                String x = curList.get(j - 1);
                String y = curList.get(j);
                graph.get(x).add(y);
                inDegree.put(y, inDegree.get(y) + 1);
            }
        }
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
