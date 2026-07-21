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

    // Minimum total weighted cost for source1 and source2 to reach destination,
    // sharing the common tail once. Returns -1 if any terminal is unreachable.
    public long minimumSharedRouteCost(int n, int[][] edges,
                                       int source1, int source2, int destination) {

        // Build undirected weighted adjacency list: edge = {u, v, weight}.
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], weight = edge[2];
            graph.get(u).add(new int[]{v, weight});
            graph.get(v).add(new int[]{u, weight});
        }

        long[] distFromS1 = dijkstra(source1, graph);
        long[] distFromS2 = dijkstra(source2, graph);
        long[] distFromD = dijkstra(destination, graph);

        long answer = Long.MAX_VALUE;

        // Try every node as the meeting point where the two routes merge.
        for (int m = 0; m < n; m++) {
            if (distFromS1[m] == Long.MAX_VALUE
                    || distFromS2[m] == Long.MAX_VALUE
                    || distFromD[m] == Long.MAX_VALUE) {
                continue;
            }
            long totalCost = distFromS1[m] + distFromS2[m] + distFromD[m];
            answer = Math.min(answer, totalCost);
        }

        return answer == Long.MAX_VALUE ? -1 : answer;
    }

    // Dijkstra shortest weighted distance from `source`; Long.MAX_VALUE if unreachable.
    private long[] dijkstra(int source, List<List<int[]>> graph) {
        int n = graph.size();
        long[] distance = new long[n];
        Arrays.fill(distance, Long.MAX_VALUE);

        // {distanceSoFar, node}, min-heap by distance.
        PriorityQueue<long[]> minHeap =
                new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));

        distance[source] = 0;
        minHeap.offer(new long[]{0, source});

        while (!minHeap.isEmpty()) {
            long[] current = minHeap.poll();
            long currentDistance = current[0];
            int currentNode = (int) current[1];

            // Skip stale heap entries.
            if (currentDistance > distance[currentNode]) {
                continue;
            }

            for (int[] next : graph.get(currentNode)) {
                int neighbor = next[0];
                int edgeWeight = next[1];
                long newDistance = currentDistance + edgeWeight;

                if (newDistance < distance[neighbor]) {
                    distance[neighbor] = newDistance;
                    minHeap.offer(new long[]{newDistance, neighbor});
                }
            }
        }
        return distance;
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
