/*
 * ============================================================================
 * Problem 4 follow-up (Google L4 prep) — Router Reachability, Per-Router Radius
 * ============================================================================
 *
 * ACCURACY NOTE: the base problem and a nearest-router-only follow-up are
 * confirmed in public Google reports; the "different radius per router" variant
 * is on the priority list but not independently verified as reported.
 *
 * THE QUESTION
 * ------------
 * Each router is {x, y, transmissionRadius}. Router A reaches B when
 * dist(A,B) <= A.radius. Because radii differ, this is DIRECTED:
 *      A=(0,0,r=10), B=(0,8,r=3): A->B ok (8<=10) but B->A fails (8>3).
 * Determine whether source can reach destination.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: DIRECTED graph reachability via BFS. Only the neighbour test changes
 * vs the base problem:
 *      current -> next  iff  dist(current, next) <= current.radius
 * i.e. the EDGE uses the CURRENT router's radius, not a shared range. BFS is
 * otherwise identical. Every router in the queue already has the message and
 * re-broadcasts with its own radius.
 *
 * APPROACHES
 *   Build full adjacency list then BFS: O(N^2) time / O(N^2) space.
 *   One-query BFS discovering neighbours on the fly: O(N^2) time / O(N) space
 *   (below).
 *
 * OVERFLOW: square coordinates/radius in long.
 * CAVEAT: assumes no two routers share the exact same coordinates.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.Queue;

public class RouterRadiusReachability {

    // routers[i] = {x, y, transmissionRadius}; source/destination = {x, y}.
    public boolean canReach(int[][] routers, int[] source, int[] destination) {
        int sourceIndex = -1;
        int destinationIndex = -1;

        for (int index = 0; index < routers.length; index++) {
            if (sameCoordinates(routers[index], source)) {
                sourceIndex = index;
            }
            if (sameCoordinates(routers[index], destination)) {
                destinationIndex = index;
            }
        }
        if (sourceIndex == -1 || destinationIndex == -1) {
            return false;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[routers.length];

        queue.offer(sourceIndex);
        visited[sourceIndex] = true;

        while (!queue.isEmpty()) {
            int currentIndex = queue.poll();

            if (currentIndex == destinationIndex) {
                return true;
            }

            int[] currentRouter = routers[currentIndex];
            // Reachable set uses the CURRENT router's radius -> directed edges.
            for (int nextIndex = 0; nextIndex < routers.length; nextIndex++) {
                if (!visited[nextIndex] && canTransmit(currentRouter, routers[nextIndex])) {
                    visited[nextIndex] = true;
                    queue.offer(nextIndex);
                }
            }
        }

        return false;
    }

    private boolean sameCoordinates(int[] router, int[] coordinate) {
        return router[0] == coordinate[0] && router[1] == coordinate[1];
    }

    private boolean canTransmit(int[] current, int[] next) {
        // long math to avoid overflow when squaring.
        long deltaX = (long) current[0] - next[0];
        long deltaY = (long) current[1] - next[1];
        long distanceSquared = deltaX * deltaX + deltaY * deltaY;

        long currentRadius = current[2];
        long radiusSquared = currentRadius * currentRadius;

        return distanceSquared <= radiusSquared;
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        RouterRadiusReachability sol = new RouterRadiusReachability();

        int[][] routers = {
            {0, 0, 10},
            {0, 8, 9},
            {0, 17, 2}
        };
        // A(0,0,r10)->B(0,8): 8<=10; B(r9)->C(0,17): 9<=9 -> reaches C.
        System.out.println(sol.canReach(routers, new int[] {0, 0}, new int[] {0, 17})); // true

        // Reverse direction fails: C(r2) can't reach B (8>2), B(r9) can't reach... 
        // actually B->A is 8<=9 ok, but C->B is 9>2 so C is stranded.
        System.out.println(sol.canReach(routers, new int[] {0, 17}, new int[] {0, 0})); // false

        // A->B ok but B->A blocked (asymmetry demo).
        int[][] two = {
            {0, 0, 10},
            {0, 8, 3}
        };
        System.out.println(sol.canReach(two, new int[] {0, 0}, new int[] {0, 8})); // true
        System.out.println(sol.canReach(two, new int[] {0, 8}, new int[] {0, 0})); // false
    }
}
