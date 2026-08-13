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

        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
        }
        for(int[] edge: edges)
        {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        Deque<Integer> q = new ArrayDeque<>();

        int[] finalCorrupted = new int[n];
        Arrays.fill(finalCorrupted, -1);
        //anything which changes from -1 is corrupted

        for(int corrupted: corruptedServers)
        {
            q.add(corrupted);
            finalCorrupted[corrupted] = 0;
        }

        while(!q.isEmpty())
        {
            int cur = q.poll();

            if(finalCorrupted[cur] > k)
            {
                break;
            }

            if(finalCorrupted[cur] == k)
            {
                continue;
            }

            for(int i = 0; i < graph.get(cur).size(); i++)
            {
                int nei = graph.get(cur).get(i);
                if(finalCorrupted[nei] == -1)
                {
                    finalCorrupted[nei] = finalCorrupted[cur] + 1;
                    q.offer(nei);
                }
            }
        }
        q.clear();
        
        if (finalCorrupted[source] != -1 ||
        finalCorrupted[destination] != -1) {
            return -1;
        }

        int[] pathDistance = new int[n];
        Arrays.fill(pathDistance, -1);

        q.offer(source);
        pathDistance[source] = 0;
        

        while(!q.isEmpty())
        {
            int cur = q.poll();
            if(cur == destination)
            {
                return pathDistance[cur];
            }

            for(int i = 0; i < graph.get(cur).size(); i++)
            {
                int nei = graph.get(cur).get(i);

                if(finalCorrupted[nei] == -1 && pathDistance[nei] == -1)
                {
                    pathDistance[nei] = pathDistance[cur] + 1;
                    q.offer(nei);
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
