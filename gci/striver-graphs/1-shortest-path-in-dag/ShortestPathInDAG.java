/*
 * ============================================================================
 * Striver Graphs #1 — Shortest Path in a Weighted DAG (source = node 0)
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given a Directed ACYCLIC Graph (DAG) with N nodes (0..N-1) and M weighted
 * edges edges[i] = {u, v, w} meaning u -> v with weight w, return the shortest
 * distance from source node 0 to every node. If a node is unreachable, its
 * distance is -1.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * In a DAG you do NOT need Dijkstra. Process nodes in TOPOLOGICAL ORDER and
 * relax outgoing edges. Because every edge goes "forward" in topo order, once
 * you reach a node its distance is already final — so one linear pass suffices.
 *
 *   1. Topo sort (DFS + stack).
 *   2. dist[] = +inf, dist[0] = 0.
 *   3. Pop nodes in topo order; skip unreachable (dist == inf); relax edges.
 *   4. Convert leftover +inf to -1.
 *
 * COMPLEXITY
 *   Time : O(N + M)      Space : O(N + M)
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class ShortestPathInDAG {

    public int[] shortestPath(int N, int M, int[][] edges) {

        List<List<int[]>> graph = new ArrayList<>();
        for(int i = 0; i < N; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < M; i++)
        {
            int u = edges[i][0];
            int v = edges[i][1];
            int wt = edges[i][2];
            graph.get(u).add(new int[]{v, wt});
        }
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[N];

        for(int i = 0; i < N; i++)
        {
            if(!visited[i])
            {
                dfsTopoSort(stack, graph, visited, i);
            }
        }
        //stack has dfsTopoSort
        int[] dist = new int[N];
        Arrays.fill(dist, (int)1e9);

        dist[0] = 0;
        while(!stack.isEmpty())
        {
            int curNode = stack.pop();
                // Only reachable nodes should relax their outgoing edges
            if (dist[curNode] == (int) 1e9) {
                continue;
            }
            //now travel all nodes adjacent to curNode and relax distance
            for(int i = 0; i < graph.get(curNode).size(); i++)
            {
                int[] nei = graph.get(curNode).get(i);
                int neiV = nei[0];
                int neiWt = nei[1];

                if(dist[curNode] + neiWt < dist[neiV])
                {
                    dist[neiV] = dist[curNode] + neiWt;
                }
            }
        }


        for(int i = 0; i < N; i++)
        {
            if(dist[i] == (int)1e9)
            {
                dist[i] = -1;
            }
        }
        return dist;

    }

    private void dfsTopoSort(Stack<Integer> stack, List<List<int[]>> graph, boolean[] visited, int node)
    {
        visited[node] = true;
        //loop through adjacent items
        for(int i = 0; i < graph.get(node).size(); i++)
        {
            //-> (v, wt)
            int neiNode = graph.get(node).get(i)[0];
            if(!visited[neiNode])
            {
                dfsTopoSort(stack, graph, visited, neiNode);
            }
        }
        stack.push(node);
    }

    // ---------------------------------------------------------------------
    // Quick self-test.
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        ShortestPathInDAG sol = new ShortestPathInDAG();

        // 1) Classic Striver example (6 nodes, 7 edges).
        //    0->1(2) 0->4(1) 1->2(3) 4->2(2) 4->5(4) 2->3(6) 5->3(1)
        int[][] e1 = {
            {0, 1, 2}, {0, 4, 1}, {1, 2, 3},
            {4, 2, 2}, {4, 5, 4}, {2, 3, 6}, {5, 3, 1}
        };
        System.out.println(Arrays.toString(sol.shortestPath(6, 7, e1)));
        // expected: [0, 2, 3, 6, 1, 5]

        // 2) Node 3 is isolated / unreachable from 0.
        int[][] e2 = { {0, 1, 1}, {1, 2, 2} };
        System.out.println(Arrays.toString(sol.shortestPath(4, 2, e2)));
        // expected: [0, 1, 3, -1]

        // 3) Single node, no edges.
        int[][] e3 = {};
        System.out.println(Arrays.toString(sol.shortestPath(1, 0, e3)));
        // expected: [0]
    }
}
