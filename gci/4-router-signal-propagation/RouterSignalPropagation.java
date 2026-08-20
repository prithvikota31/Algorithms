/*
 * ============================================================================
 * Problem 4 (Google L4 prep) — Router Signal Propagation (reachability)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Routers at 2D coordinates. A router's signal reaches any router within a
 * fixed transmission `range` (Euclidean). Signal hops router-to-router.
 * Determine whether `source` can reach `destination`.
 *
 * Reported Google follow-up (NOT solved here): each router has its OWN radius,
 * so an edge u->v exists when dist(u,v) <= radius[u] (directed / asymmetric).
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: implicit graph + BFS reachability. Node = router; edge when two
 * routers are within range. "Can source reach destination?" = plain BFS/DFS.
 *
 * Compare squared distances to avoid sqrt / floating point:
 *      dx*dx + dy*dy <= range*range
 * Mark visited on enqueue so each router is processed once.
 *
 * APPROACH
 *   Build the undirected graph, then run BFS from source to destination.
 *     Time O(V^2) (each pair checked) / Space O(V + E), which is O(V^2) in
 *     the worst case. Neighbours can instead be scanned during BFS for O(V)
 *     auxiliary space, or spatially indexed when V is large.
 *
 * CAVEAT: assumes no two routers share the exact same coordinates.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class RouterSignalPropagation {

    public boolean canReach(
            List<int[]> routers,
            int[] source,
            int[] destination,
            int range) {

        //build a graph
        //assume each indice as a vertice,
        // so we have vertices from 0 to n - 1
        int n = routers.size();
        List<List<Integer>> graph = new ArrayList<>();
        int src = -1;
        int des = -1;
        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
            if(isSame(source, routers.get(i)))
            {
                src = i;
            }
            if(isSame(destination, routers.get(i)))
            {
                des = i;
            }
        }

        if (src == -1 || des == -1) {
            return false;
        }
        if(src == des)
        {
            return true;
        }

        //build graph
        //graph node is represented by indice
        for(int i = 0 ; i < n; i++)
        {
            int[] u = routers.get(i);
            for(int j = i + 1; j < n; j++)
            {
                int[] v = routers.get(j);
                if(isWithInRange(u, v, range))
                {
                    graph.get(i).add(j);
                    graph.get(j).add(i);
                }
            }
        }
        //lets start bfs and see if we can reach destination
        Deque<Integer> q = new ArrayDeque<>();
        int[] visited = new int[n];
        visited[src] = 1;
        q.offer(src);
        while(!q.isEmpty())
        {
            int cur = q.poll();
            if(cur == des)
            {
                return true;
            }
            for(int nei: graph.get(cur))
            {
                if(visited[nei] == 0)
                {
                    visited[nei] = 1;
                    q.offer(nei);
                }
            }
        }

        return false;

    }


    private boolean isSame(int[] p1, int[] p2)
    {
        return p1[0] == p2[0] && p1[1] == p2[1];
    }

    private boolean isWithInRange(int[] p1, int[] p2, int radius)
    {
        long delX = (long)p1[0] - p2[0];
        long delY = (long)p1[1] - p2[1];

        return delX * delX + delY * delY <= (long)radius * radius;
    }


    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        RouterSignalPropagation sol = new RouterSignalPropagation();

        List<int[]> routers = Arrays.asList(
            new int[] {0, 0},
            new int[] {1, 0},
            new int[] {3, 0},
            new int[] {5, 0}
        );

        // range 2: 0-1 (d^2=1), 1-3 (d^2=4), 3-5 (d^2=4) all <= 4 -> chain reaches.
        System.out.println(sol.canReach(routers, new int[] {0, 0}, new int[] {5, 0}, 2)); // true

        // range 1: only 0-1 connects (d^2=1); the (1,0)->(3,0) gap (d^2=4) breaks it.
        System.out.println(sol.canReach(routers, new int[] {0, 0}, new int[] {5, 0}, 1)); // false

        // source == destination.
        System.out.println(sol.canReach(routers, new int[] {0, 0}, new int[] {0, 0}, 1)); // true
    }
}
