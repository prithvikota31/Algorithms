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
import java.util.Deque;

public class RouterRadiusReachability {

    // routers[i] = {x, y, transmissionRadius}; source/destination = {x, y}.
    public boolean canReach(int[][] routers, int[] source, int[] destination) {
        int src = -1;
        int dst = -1;

        for(int i = 0; i < routers.length; i++)
        {
            if(isSameCoordinate(routers[i], source))
            {
                src = i;
            }
            if(isSameCoordinate(routers[i], destination))
            {
                dst = i;
            }
        }

        if(src == -1 || dst == -1)
        {
            return false;
        }

        Deque<Integer> q = new ArrayDeque<>(); // it contains indices of routers
        q.offer(src);
        int n = routers.length;
        int[] visited = new int[n];
        visited[src] = 1;
        while(!q.isEmpty())
        {
            int cur = q.poll();
            if(cur == dst)
            {
                return true;
            }

            for(int i = 0; i < n; i++)
            {
                int nei = i;
                if(visited[nei] == 0 && canBeReached(routers[cur], routers[nei]))
                {
                    visited[nei] = 1;
                    q.offer(nei);
                }
            }
        }
        return false;
    }

    private boolean canBeReached(int[] src, int[] router)
    {
        int delX = Math.abs(src[0] - router[0]);
        int delY = Math.abs(src[1] - router[1]);
        int radiusSquare = src[2] * src[2];

        return delX * delX + delY * delY <= radiusSquare;
    }

    private boolean isSameCoordinate(int[] router, int[] p)  
    {
        return router[0] == p[0] && router[1] == p[1];
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
