/*
 * ============================================================================
 * Striver Graphs #2 — Dijkstra's Shortest Path (undirected weighted graph)
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given an UNDIRECTED weighted graph with V vertices and an edge list where
 * each edge is {u, v, w}, return the shortest distance from a source S to every
 * vertex. All weights are non-negative.
 *
 *   edges: ArrayList of {u, v, w} triples (each edge added both ways: u<->v).
 *   return: int[] dist, dist[i] = shortest distance from S to i.
 *           Unreachable vertices keep the sentinel 1e9.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Dijkstra = greedy BFS with a MIN-HEAP keyed by tentative distance.
 * Always expand the closest unfinalized node; when it pops, its distance is
 * final (true because all weights >= 0). Relax its neighbors and push updates.
 *
 * The heap can hold STALE copies of a node (an older, larger distance). Guard
 * with `if (curDist > dist[curNode]) continue;` to skip them — this replaces
 * a visited[] array and keeps it correct.
 *
 * COMPLEXITY
 *   Time : O((V + E) log V)     Space : O(V + E)
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class Dijkstra {

    public int[] dijkstra(int V, ArrayList<ArrayList<Integer>> edges, int S)
    {
       // -> (u, v, wt)

       List<List<int[]>> graph = new ArrayList<>();
       for(int i = 0; i < V; i++)
       {
            graph.add(new ArrayList<>());
       }
       for(int i = 0; i < edges.size(); i++)
       {
            int u = edges.get(i).get(0);
            int v = edges.get(i).get(1);
            int wt = edges.get(i).get(2);
            graph.get(u).add(new int[]{v, wt});
            graph.get(v).add(new int[]{u, wt});
       }
        //int[] has {dist, node}
       PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
       pq.offer(new int[]{0, S});
       int[] dist = new int[V];
       Arrays.fill(dist, (int)1e9);
       dist[S] = 0;


       while(!pq.isEmpty())
       {
            int[] cur = pq.poll();
            int curNode = cur[1];
            int curDist = cur[0];

            if(curDist > dist[curNode])
            {
                continue;
            }

            //now traverse along vertices connected to curNode, and relax dist
            for(int i = 0; i < graph.get(curNode).size(); i++)
            {
                int[] next = graph.get(curNode).get(i);
                int nextNode = next[0];
                int nextWt = next[1];
                if(curDist + nextWt < dist[nextNode])
                {
                    //relax
                    dist[nextNode] = curDist + nextWt;
                    pq.offer(new int[]{dist[nextNode], nextNode});
                }
            }
       }

       return dist;

    }

    // ---------------------------------------------------------------------
    // Quick self-test.
    // ---------------------------------------------------------------------
    private static ArrayList<ArrayList<Integer>> buildEdges(int[][] triples) {
        ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
        for (int[] t : triples) {
            edges.add(new ArrayList<>(Arrays.asList(t[0], t[1], t[2])));
        }
        return edges;
    }

    public static void main(String[] args) {
        Dijkstra sol = new Dijkstra();

        // 1) Connected undirected graph, source 0.
        //    0-1(2) 0-2(4) 1-2(1) 1-3(7) 2-4(3) 4-3(2)
        int[][] e1 = { {0,1,2}, {0,2,4}, {1,2,1}, {1,3,7}, {2,4,3}, {4,3,2} };
        System.out.println(Arrays.toString(sol.dijkstra(5, buildEdges(e1), 0)));
        // expected: [0, 2, 3, 8, 6]

        // 2) Vertex 3 is isolated -> stays at sentinel 1e9 (1000000000).
        int[][] e2 = { {0,1,1}, {1,2,2} };
        System.out.println(Arrays.toString(sol.dijkstra(4, buildEdges(e2), 0)));
        // expected: [0, 1, 3, 1000000000]

        // 3) Single vertex, no edges.
        int[][] e3 = {};
        System.out.println(Arrays.toString(sol.dijkstra(1, buildEdges(e3), 0)));
        // expected: [0]
    }
}
