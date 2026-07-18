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
import java.util.List;
import java.util.Queue;

public class RouterSignalPropagation {

    public boolean canReach(
            List<int[]> routers,
            int[] source,
            int[] destination,
            int range) {

        int sourceIndex = -1;
        int destinationIndex = -1;

        // Map the source/destination coordinates to their router indices.
        for (int index = 0; index < routers.size(); index++) {
            int[] router = routers.get(index);
            if (sameCoordinate(router, source)) {
                sourceIndex = index;
            }
            if (sameCoordinate(router, destination)) {
                destinationIndex = index;
            }
        }
        if (sourceIndex == -1 || destinationIndex == -1) {
            return false;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[routers.size()];

        queue.offer(sourceIndex);
        visited[sourceIndex] = true;

        int rangeSquared = range * range;

        while (!queue.isEmpty()) {
            int currentIndex = queue.poll();

            if (currentIndex == destinationIndex) {
                return true;
            }

            int[] currentRouter = routers.get(currentIndex);
            // Every unvisited router within range receives the signal.
            for (int nextIndex = 0; nextIndex < routers.size(); nextIndex++) {
                if (!visited[nextIndex]
                        && isWithinRange(currentRouter, routers.get(nextIndex), rangeSquared)) {
                    visited[nextIndex] = true;
                    queue.offer(nextIndex);
                }
            }
        }

        return false;
    }

    private boolean sameCoordinate(int[] first, int[] second) {
        return first[0] == second[0] && first[1] == second[1];
    }

    private boolean isWithinRange(int[] first, int[] second, int rangeSquared) {
        int deltaX = first[0] - second[0];
        int deltaY = first[1] - second[1];
        return deltaX * deltaX + deltaY * deltaY <= rangeSquared;
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
