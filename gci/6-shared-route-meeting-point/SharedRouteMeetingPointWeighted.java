/*
 * ============================================================================
 * Problem 6 — FOLLOW-UP: Two Sources with WEIGHTED Edges
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Same as the base (two sources reach one destination, shared route counted
 * once), but edges now carry positive weights and we minimize TOTAL COST.
 * This is essentially LeetCode 2203.
 *
 * MENTAL MAP
 * ----------
 * Identical meeting-node idea, but shortest paths in a weighted graph need
 * Dijkstra instead of BFS:
 *      answer = min over every meeting node M of
 *               distFromS1[M] + distFromS2[M] + distFromD[M]
 * S1->M and S2->M are separate; M->D is the shared tail, counted once.
 *
 * Run Dijkstra from S1, S2, and D (D too, since the graph is undirected so
 * distFromD[M] == distFromM[D]). Then scan every node as M.
 *
 * Complexity: 3x Dijkstra -> O((V + E) log V) overall, O(V + E) space.
 * Use long distances to avoid overflow when summing three path costs.
 * ============================================================================
 */

import java.util.*;

public class SharedRouteMeetingPointWeighted {

    private static final long INF = Long.MAX_VALUE;

    // Minimum total weighted cost for source1 and source2 to reach destination,
    // sharing the common tail once. Returns -1 if any terminal is unreachable.
    public long minimumSharedRouteCost(int n, int[][] edges,
                                       int alice, int bob, int destination) {

        //construct graph from edges
        List<List<int[]>> graph = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
        }
        for(int[] edge : edges)
        {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w});
        }

        //find distance of each ndoe from alice, bob and destination
        //then for each node, find the sum of didtances to each of them 
        //minimize the distance (the node will be kind of meeting point)
        long[] distanceFromAlice = diktras(graph, alice);
        long[] distanceFromBob = diktras(graph, bob);
        long[] distanceFromDest = diktras(graph, destination);

        long minDistinct = Long.MAX_VALUE;

        for(int i = 0; i < n; i++)
        {
            if(distanceFromAlice[i] != INF && distanceFromBob[i] != INF && distanceFromDest[i] != INF)
            {
                long d = safeAdd(safeAdd(distanceFromAlice[i], distanceFromBob[i]), distanceFromDest[i]);
                minDistinct = Math.min(minDistinct, d);
            }
        }

        return minDistinct == Long.MAX_VALUE? -1: minDistinct;
    }

    private long[] diktras(List<List<int[]>> graph, int src)
    {
        int n = graph.size();
        long[] dist = new long[n];

        Arrays.fill(dist, INF);
        dist[src] = 0;
        //each element (dist, node)
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.offer(new long[]{0, src});
        while(!pq.isEmpty())
        {
            long[] cur = pq.poll();
            long cDist = cur[0];
            int cNode = (int)cur[1];

            if(cDist != dist[cNode])
            {
                continue;
            }

            for(int[] nei: graph.get(cNode))
            {
                int neiNode = nei[0];
                int neiEdgeWt = nei[1];
                long candidateDistance = safeAdd(cDist, neiEdgeWt);

                if(candidateDistance < dist[neiNode])
                {
                    dist[neiNode] = candidateDistance;
                    pq.offer(new long[]{candidateDistance, neiNode});
                }
            }

        }
        return dist;
    }

    private long safeAdd(long first, long second)
    {
        if(first == INF || second > INF - first)
        {
            return INF;
        }
        return first + second;
    }

  



    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        SharedRouteMeetingPointWeighted sol = new SharedRouteMeetingPointWeighted();

        // Uniform weights mirror the unweighted dry run: answer 4.
        int[][] edges1 = {{0, 1, 1}, {1, 2, 1}, {2, 3, 1}, {4, 2, 1}};
        System.out.println(sol.minimumSharedRouteCost(5, edges1, 0, 4, 3)); // 4

        // Weights matter: both merge at node 2 (== D). 0->1->2 = 2, 3->2 = 1 -> 3.
        int[][] edges2 = {{0, 1, 1}, {0, 2, 5}, {1, 2, 1}, {2, 3, 1}};
        System.out.println(sol.minimumSharedRouteCost(4, edges2, 0, 3, 2)); // 3

        // Disconnected: node 4 cannot reach D.
        int[][] edges3 = {{0, 1, 1}, {1, 2, 1}, {2, 3, 1}};
        System.out.println(sol.minimumSharedRouteCost(5, edges3, 0, 4, 3)); // -1
    }
}
