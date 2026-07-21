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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class SharedRouteMeetingPoint {

    // Minimum number of distinct edges so that both `alice` and `bob` reach
    // `destination`, sharing edges where possible. Returns -1 if impossible.
    public int minimumDistinctEdges(int n, int[][] edges,
                                    int alice, int bob, int destination) {

        // Build the undirected adjacency list.
        List<List<Integer>> graph = new ArrayList<>();
        for (int node = 0; node < n; node++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // Shortest distance from each terminal to every node.
        int[] distAlice = bfs(alice, graph);
        int[] distBob = bfs(bob, graph);
        int[] distDest = bfs(destination, graph);

        int minimumEdges = Integer.MAX_VALUE;

        // Try every node as the meeting point M where the two routes merge.
        for (int m = 0; m < n; m++) {
            // All three terminals must be able to reach M.
            if (distAlice[m] == -1 || distBob[m] == -1 || distDest[m] == -1) {
                continue;
            }
            int distinctEdges = distAlice[m] + distBob[m] + distDest[m];
            minimumEdges = Math.min(minimumEdges, distinctEdges);
        }

        return minimumEdges == Integer.MAX_VALUE ? -1 : minimumEdges;
    }

    // BFS shortest distance from `source` to every node; -1 if unreachable.
    private int[] bfs(int source, List<List<Integer>> graph) {
        int n = graph.size();
        int[] distance = new int[n];
        Arrays.fill(distance, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        distance[source] = 0;
        queue.offer(source);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int neighbor : graph.get(current)) {
                // First time we reach a node is its shortest distance (unweighted).
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[current] + 1;
                    queue.offer(neighbor);
                }
            }
        }
        return distance;
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
