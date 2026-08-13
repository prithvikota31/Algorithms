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
 * APPROACHES
 *   Optimal (uniform range): BFS over routers, neighbours = all within range.
 *     Time O(V^2) (each pair checked) / Space O(V). For dense range checks V^2
 *     is unavoidable without spatial indexing (grid/k-d tree) — a valid
 *     optimization to mention if V is large.
 *
 * CAVEAT: assumes no two routers share the exact same coordinates.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class RouterSignalPropagation {

    public boolean canReach(
            List<int[]> routers,
            int[] source,
            int[] destination,
            int range) {

        int src = -1;
        int des = -1;
        for(int i = 0; i < routers.size(); i++)
        {
            if(isSame(routers.get(i), source))
            {
                src = i;
            }
            if(isSame(routers.get(i), destination))
            {
                des = i;
            }
        }

        if(src == -1 || des == -1)
        {
            return false;
        }

        Deque<int[]> q = new ArrayDeque<>();
        q.offer(routers.get(src));
        int n = routers.size();
        boolean[] visited = new boolean[n];
        visited[src] = true;

        int rangeSquared = range * range;
        while(!q.isEmpty())
        {
            int[] cur = q.poll();

            if(isSame(cur, destination))
            {
                return true;
            }
            
            for(int i = 0; i < n; i++)
            {
                if(!visited[i] && isWithinRange(cur, routers.get(i), rangeSquared))
                {
                    q.offer(routers.get(i));
                    visited[i] = true;
                }
            }

        }
        return false;
    }

    private boolean isWithinRange(int[] cur, int[] nei, int r2)
    {
        int delX = Math.abs(cur[0] - nei[0]);
        int delY = Math.abs(cur[1] - nei[1]);

        return delX * delX + delY * delY <= r2;
    }

    private boolean isSame(int[] p1, int[] p2)
    {
        return p1[0] == p2[0] && p1[1] == p2[1];
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
