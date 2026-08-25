/*
 * ============================================================================
 * Problem 3 follow-up (Google L4 prep) — Corruption Spreads K Hops,
 *                                          then Shortest Safe Path
 * ============================================================================
 *
 * SOURCE NOTE: reported in a 2025 Google L3 phone screen (still a strong
 * L4-level graph variation). Interviewer escalated: reachability -> shortest
 * distance -> corruption spreading up to K edges. This is the GRAPH variant of
 * the "danger field then safe path" idea that row #2 owns; kept here as a
 * multi-source-BFS follow-up. Row #2 stays open for its own (grid) form.
 *
 * THE QUESTION
 * ------------
 * Undirected, unweighted server network. Some servers start corrupted.
 * Corruption spreads to every server within K edges of any corrupted server.
 * Corrupted servers cannot transfer data. Return the shortest safe path (edge
 * count) from source to destination, or -1 if none.
 * (Corruption finishes spreading BEFORE data transfer begins.)
 *
 * EXAMPLE
 *   edges: 0-1,1-2,2-3,3-4,0-5,5-6,6-4   corrupted=[2]  K=1
 *   Server 2 blocks {1,2,3}. Top path 0-1-2-3-4 is dead; 0-5-6-4 is safe -> 3.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * TWO BFS chained:
 *   1) multi-source BFS from all corrupted servers, expand only through
 *      distance K  -> every reached node is BLOCKED.
 *   2) plain BFS from source through SAFE nodes only -> shortest safe path.
 *
 * Keep the two distance meanings separate:
 *   dangerDistance[v] = hops from nearest corruption (-1 = safe/unreached)
 *   pathDistance[v]   = hops from source
 *
 * APPROACHES
 *   Brute force : separate BFS per corrupted server -> O(C*(V+E)).
 *   Optimal     : one multi-source BFS + one path BFS -> O(V+E) time/space.
 *
 * Memory trick: first BFS builds the danger map; second BFS navigates the safe
 * map.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class CorruptionSpreadKHops {

    public int shortestSafePath(
            int n,
            int[][] edges,
            int source,
            int destination,
            int[] corruptedServers,
            int k) {

        // Build the undirected adjacency list.
        List<List<Integer>> graph = new ArrayList<>();
        for (int node = 0; node < n; node++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int firstNode = edge[0];
            int secondNode = edge[1];
            graph.get(firstNode).add(secondNode);
            graph.get(secondNode).add(firstNode);
        }

        // Phase 1: multi-source BFS from every initially corrupted server.
        // dangerDistance[node] == -1 means the node remains safe.
        int[] dangerDistance = new int[n];
        Arrays.fill(dangerDistance, -1);

        Deque<Integer> queue = new ArrayDeque<>();
        for (int corruptedServer : corruptedServers) {
            if (dangerDistance[corruptedServer] == -1) {
                dangerDistance[corruptedServer] = 0;
                queue.offer(corruptedServer);
            }
        }

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();

            // This node is blocked, but corruption cannot spread beyond K hops.
            if (dangerDistance[currentNode] == k) {
                continue;
            }

            for (int adjacentNode : graph.get(currentNode)) {
                if (dangerDistance[adjacentNode] == -1) {
                    dangerDistance[adjacentNode] = dangerDistance[currentNode] + 1;
                    queue.offer(adjacentNode);
                }
            }
        }

        // A path cannot begin or end on a blocked server.
        if (dangerDistance[source] != -1
                || dangerDistance[destination] != -1) {
            return -1;
        }

        // Phase 2: ordinary BFS through safe servers only.
        int[] pathDistance = new int[n];
        Arrays.fill(pathDistance, -1);

        queue.offer(source);
        pathDistance[source] = 0;

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();

            if (currentNode == destination) {
                return pathDistance[currentNode];
            }

            for (int adjacentNode : graph.get(currentNode)) {
                boolean isSafe = dangerDistance[adjacentNode] == -1;
                boolean isUnvisited = pathDistance[adjacentNode] == -1;

                if (isSafe && isUnvisited) {
                    pathDistance[adjacentNode] = pathDistance[currentNode] + 1;
                    queue.offer(adjacentNode);
                }
            }
        }

        return -1;
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        CorruptionSpreadKHops sol = new CorruptionSpreadKHops();

        int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 4},
            {0, 5}, {5, 6}, {6, 4}
        };

        System.out.println(sol.shortestSafePath(7, edges, 0, 4, new int[] {2}, 1)); // 3
        System.out.println(sol.shortestSafePath(7, edges, 0, 4, new int[] {2}, 2)); // -1 (K=2 blocks 0 and 4 too)
        System.out.println(sol.shortestSafePath(7, edges, 0, 4, new int[] {},  1)); // 3 (no corruption; top path 0-1-2-3-4 len 4, lower 0-5-6-4 len 3)
    }
}
