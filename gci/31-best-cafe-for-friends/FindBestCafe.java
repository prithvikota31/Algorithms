/*
 * ============================================================================
 * Problem 31 (Google L4 prep) — Best Café for All Friends
 *                               (minimize the MAX friend distance)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Unweighted undirected graph. Some nodes hold friends, some hold cafés.
 * Choose the café where the farthest-traveling friend walks the least:
 *      score(cafe)  = max over friends of dist(friend, cafe)
 *      answer       = café with the minimum score
 * Return -1 if no café is reachable by EVERY friend.
 *
 * EXAMPLE
 *   friends=[1,7]  cafes=[5,6]
 *   edges: 1-2,2-3,3-4,4-5,3-6,7-5,7-3
 *              cafe5  cafe6
 *   friend1     4      3
 *   friend7     1      2
 *   max         4      3   -> answer = cafe 6
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * CRITICAL distinction: a single multi-source BFS from all friends gives the
 * MINIMUM friend distance to each café — wrong here. We need the MAXIMUM across
 * friends. e.g. friend distances [1, 10] -> multi-source BFS says 1, but the
 * fair score is 10. One shared visited[] loses the other friends' info.
 *
 * So: BFS separately FROM EACH FRIEND, and for each café keep
 *      maxDistance[cafe] = max(maxDistance[cafe], distFromThisFriend)
 * mark a café invalid the moment any friend can't reach it. Answer = valid café
 * with the smallest maxDistance.
 *
 * APPROACHES
 *   Brute force : enumerate paths / Floyd-Warshall O(V^3) — computes all-pairs
 *                 though we only care about friends x cafés.
 *   Optimal     : one BFS per friend, track per-café max (below).
 *                 Time O(F*(V+E) + F*C)   Space O(V + C)   (reuse dist[]).
 *
 * Memory trick: nearest source -> MIN; fair meeting place -> track the MAX,
 * then minimize it.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class FindBestCafe {

    public int findBestCafe(int n, int[][] edges, int[] friends, int[] cafes) {
        // Build the undirected graph.
        List<List<Integer>> graph = new ArrayList<>();
        for (int node = 0; node < n; node++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // maxDistance[i] = worst friend distance to cafes[i] so far;
        // valid[i] = false once some friend cannot reach it.
        int[] maxDistance = new int[cafes.length];
        boolean[] valid = new boolean[cafes.length];
        Arrays.fill(valid, true);

        // One BFS per friend.
        for (int friend : friends) {
            int[] distance = bfs(friend, graph);

            for (int i = 0; i < cafes.length; i++) {
                if (!valid[i]) {
                    continue;
                }
                int cafe = cafes[i];
                if (distance[cafe] == -1) {
                    valid[i] = false;                 // this friend can't reach it
                } else {
                    maxDistance[i] = Math.max(maxDistance[i], distance[cafe]);
                }
            }
        }

        // Pick the valid café with the smallest worst-case distance.
        int bestCafe = -1;
        int bestMaximumDistance = Integer.MAX_VALUE;
        for (int i = 0; i < cafes.length; i++) {
            if (valid[i] && maxDistance[i] < bestMaximumDistance) {
                bestMaximumDistance = maxDistance[i];
                bestCafe = cafes[i];
            }
        }
        return bestCafe;
    }

    private int[] bfs(int source, List<List<Integer>> graph) {
        int[] distance = new int[graph.size()];
        Arrays.fill(distance, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        distance[source] = 0;
        queue.offer(source);

        // Unweighted -> BFS gives shortest distances.
        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            for (int neighbor : graph.get(currentNode)) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[currentNode] + 1;
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
        FindBestCafe sol = new FindBestCafe();

        int[][] edges = {
            {1, 2}, {2, 3}, {3, 4}, {4, 5}, {3, 6}, {7, 5}, {7, 3}
        };
        System.out.println(sol.findBestCafe(8, edges, new int[] {1, 7}, new int[] {5, 6})); // 6

        // Single friend -> just the nearest café.
        System.out.println(sol.findBestCafe(8, edges, new int[] {1}, new int[] {5, 6}));    // 6 (dist 3 < 4)

        // A café unreachable by one friend is rejected.
        int[][] edges2 = {
            {0, 1}, {1, 2},          // friend 0 side
            {3, 4}                   // isolated component holds café 4
        };
        System.out.println(sol.findBestCafe(5, edges2, new int[] {0, 3}, new int[] {2, 4})); // -1
    }
}
