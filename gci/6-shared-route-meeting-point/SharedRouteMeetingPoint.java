/*
 * ============================================================================
 * Problem 6 (Google L4 prep) — Two People Sharing a Route to a Destination
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Undirected, UNWEIGHTED graph. Alice starts at node `alice`, Bob starts at
 * node `bob`, and both must reach the destination `D`. They may share parts of
 * their routes; a shared edge is counted ONCE. Minimize the number of DISTINCT
 * edges used across both routes. Return -1 if either cannot reach D.
 *
 * Reported follow-up (NOT solved here): generalize from two people to MULTIPLE
 * sources.  [inferred] follow-up: WEIGHTED graph -> replace BFS with Dijkstra
 * (this weighted variant is essentially LeetCode 2203).
 *
 * WHY MULTI-SOURCE DOESN'T GENERALIZE (conceptual — deliberately not coded):
 *   With >2 people, a SINGLE merge node M is no longer enough: groups can merge
 *   gradually (S1,S2 merge at X, then merge with S3 at Y). The unrestricted
 *   problem is a STEINER TREE — the smallest connected subgraph containing all
 *   required terminals — which is NP-hard. Recognizing that is the high-value
 *   insight. Tractable only under special structure:
 *     - everyone must merge at ONE node  -> this 3xBFS meeting-node method;
 *     - graph is a TREE                   -> union of unique source->D paths;
 *     - very small k (#sources)           -> subset DP dp[mask][v] = min cost to
 *         connect the sources in `mask` through node v; combine submasks at the
 *         same v: dp[full][v] = min over splits (dp[sub][v] + dp[full^sub][v]),
 *         then relax along edges; answer = dp[(1<<k)-1][D]. Size 2^k x n.
 *
 * EXAMPLE
 *   A -- X -- Y -- D , with B also attached to Y
 *   Alice: A->X->Y->D ; Bob: B->Y->D. Y->D is shared -> counts once.
 *   Distinct edges {A-X, X-Y, Y-D, B-Y} = 4.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * The optimal union of the two paths is a TREE joining the three terminals
 * A, B, D. It has a single branch/meeting node M:
 *      A -> M , B -> M , M -> D
 * For a fixed M each segment must itself be a shortest path (else shorten it),
 * so the distinct-edge cost is distA[M] + distB[M] + distD[M].
 *
 *      answer = min over every node M of  distA[M] + distB[M] + distD[M]
 *
 * Get all three distance arrays with three BFS runs (BFS = shortest path in an
 * unweighted graph), then scan every node as the candidate meeting point.
 *
 * APPROACHES
 *   Brute force : enumerate path pairs, union their edges -> exponential.
 *   Optimal     : 3x BFS + scan nodes.  Time O(V + E)   Space O(V + E).
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class SharedRouteMeetingPoint {

    // Minimum number of distinct edges so that both `alice` and `bob` reach
    // `destination`, sharing edges where possible. Returns -1 if impossible.
    public int minimumDistinctEdges(int n, int[][] edges,
                                    int alice, int bob, int destination) {
        //construct graph
        List<List<Integer>> graph = new ArrayList<>();
    
        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < edges.length; i++)
        {
            int u = edges[i][0];
            int v = edges[i][1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        int[] distA = bfs(alice, graph);
        int[] distB = bfs(bob, graph);
        int[] distD = bfs(destination, graph);
        int ans = Integer.MAX_VALUE;
        for(int i = 0; i < n; i++)
        {
            if(distA[i] != (int)1e9 && distB[i] != (int)1e9 && distD[i] != (int)1e9)
            {
                ans = Math.min(ans, distA[i] + distB[i] + distD[i]);
            }
        }

        return ans == Integer.MAX_VALUE? -1: ans;
    }

    // BFS shortest distance from `source` to every node; -1 if unreachable.
    private int[] bfs(int source, List<List<Integer>> graph) {
        int n = graph.size();
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(source);
        int[] dist = new int[n];
        Arrays.fill(dist, (int)1e9);
        dist[source] = 0;
        while(!q.isEmpty())
        {
            int cur = q.poll();
            for(int nei: graph.get(cur))
            {
                if(dist[nei] == (int)1e9)
                {
                    dist[nei] = dist[cur] + 1;
                    q.offer(nei);
                }
            }
        }

        return dist;

    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        SharedRouteMeetingPoint sol = new SharedRouteMeetingPoint();

        // Dry run: 0-1, 1-2, 2-3, 4-2 ; Alice=0, Bob=4, D=3.
        // Alice 0->1->2->3, Bob 4->2->3, share 2-3 -> {0-1,1-2,4-2,2-3} = 4.
        int[][] edges1 = {{0, 1}, {1, 2}, {2, 3}, {4, 2}};
        System.out.println(sol.minimumDistinctEdges(5, edges1, 0, 4, 3)); // 4

        // Both already at destination.
        System.out.println(sol.minimumDistinctEdges(5, edges1, 3, 3, 3)); // 0

        // Straight line 0-1-2-3-4 ; Alice=0, Bob=4, D=2 -> 2 + 2 + 0 = 4.
        int[][] edges2 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}};
        System.out.println(sol.minimumDistinctEdges(5, edges2, 0, 4, 2)); // 4

        // Disconnected: Bob (node 4) cannot reach D.
        int[][] edges3 = {{0, 1}, {1, 2}, {2, 3}};
        System.out.println(sol.minimumDistinctEdges(5, edges3, 0, 4, 3)); // -1
    }
}
