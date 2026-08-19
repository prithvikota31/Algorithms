/*
 * ============================================================================
 * Problem 47 (Google L4 prep) - Best Root for a Binary Tree
 * ============================================================================
 *
 * PROMPT
 *   Given an undirected tree, choose a root so every node has at most two
 *   children. Return any valid root, or -1 if no valid root exists.
 *
 * EXAMPLES
 *   n=4, edges=[[0,1],[1,2],[1,3]] -> 0, 2, or 3
 *   n=5, edges=[[0,1],[0,2],[0,3],[0,4]] -> -1
 *   n=1, edges=[] -> 0
 *
 * INTUITION
 *   Rooting consumes one edge as the parent edge for every non-root node.
 *   Therefore, the root has degree(root) children, while every other node has
 *   degree(node) - 1 children. A valid root needs degree at most 2, and every
 *   other node needs degree at most 3.
 *
 * ALGORITHM
 *   1. Count each node's degree from the undirected edges.
 *   2. If any degree exceeds 3, return -1.
 *   3. Return any node whose degree is at most 2.
 *
 * COMPLEXITY
 *   Time O(N), because a tree has N - 1 edges.
 *   Space O(N) for the degree array.
 * ============================================================================
 */
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class BestRootForBinaryTree {

    public int findRoot(int nodeCount, int[][] edges) {
        // no nodes no root
        if(nodeCount == 0)
        {
            return -1;
        }

        int[] degrees = findDegree(nodeCount, edges);

        for(int i = 0; i < degrees.length; i++)
        {
            if(degrees[i] > 3)
            {
                return -1;
            }
        }

        for(int i = 0; i < degrees.length; i++)
        {
            if(degrees[i] <= 2)
            {
                return i;
            }
        }

        return -1;

    }
    private int[] findDegree(int nodeCount, int[][] edges)
    {
        int[] degree = new int[nodeCount];
        for(int[] edge: edges)
        {
            int x = edge[0];
            int y = edge[1];
            degree[x]++;
            degree[y]++;
        }
        return degree;
    }

    /*
     * Follow-up: among valid roots, minimize the resulting tree height.
     * A node's height as root is its maximum distance to either endpoint of
     * the tree's diameter. Compute those two distance arrays, then choose the
     * valid root with minimum maximum distance.
     */
    public static class BfsResult{
        int farthestNode;
        int[] distance;
    }
    public int findMinimumHeightRoot(int nodeCount, int[][] edges) {
        //find one farthest Node
        List<List<Integer>> graph = new ArrayList<>();
        buildgraph(graph, edges, nodeCount);
        //do bfs from any end to find farthest node, which will one end of diameter
        //from there if we find farthest we get another end of diameter
        if (nodeCount == 0) {
            return -1;
        }

        for (List<Integer> neighbors : graph) {
            if (neighbors.size() > 3) {
                return -1;
            }
        }

        BfsResult rFromRandomSource = bfs(graph, 0);
        BfsResult rDiameterFirstEnd = bfs(graph, rFromRandomSource.farthestNode);
        BfsResult rDiameterSecondEnd = bfs(graph, rDiameterFirstEnd.farthestNode);

        //first end and secodnend results have distances from each diameter end
        int bestRoot = -1;
        int minHeight = Integer.MAX_VALUE;

        for(int i = 0; i < nodeCount; i++)
        {
            if (graph.get(i).size() > 2) {
                continue;
            }
            int heightFromNode = Math.max(rDiameterFirstEnd.distance[i], rDiameterSecondEnd.distance[i]);
            if(heightFromNode < minHeight)
            {
                minHeight = heightFromNode;
                bestRoot = i;
            }
        }

        return bestRoot;

    }

    private BfsResult bfs(List<List<Integer>> graph, int source)
    {
        int n = graph.size();
        int[] distance = new int[n];
        Arrays.fill(distance, -1);

        Deque<Integer> q = new ArrayDeque<>();

        q.offer(source);
        distance[source] = 0;
        int farthest = source;

        while(!q.isEmpty())
        {
            int cur = q.poll();
            if(distance[cur] > distance[farthest])
            {
                farthest = cur;
            }
            for(int nei: graph.get(cur))
            {
                if(distance[nei] == -1)
                {
                    distance[nei] = 1 + distance[cur];
                    q.offer(nei);
                }
            }
        }
        BfsResult result = new BfsResult();
        result.distance = distance;
        result.farthestNode = farthest;

        return result;
    }



    private void buildgraph(List<List<Integer>> graph, int[][] edges, int nodeCount)
    {
        for(int i = 0; i < nodeCount; i++)
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
    }




    public static void main(String[] args) {
        BestRootForBinaryTree solution = new BestRootForBinaryTree();

        int[][] branchingTree = {{0, 1}, {1, 2}, {1, 3}};
        check("degree-three internal node", solution.findRoot(4, branchingTree), 0);
        check("minimum-height valid root",
                solution.findMinimumHeightRoot(4, branchingTree), 0);

        int[][] path = {{0, 1}, {1, 2}, {2, 3}, {3, 4}};
        check("path", solution.findRoot(5, path), 0);
        check("path minimum-height root", solution.findMinimumHeightRoot(5, path), 2);

        int[][] degreeFour = {{0, 1}, {0, 2}, {0, 3}, {0, 4}};
        check("degree-four impossible", solution.findRoot(5, degreeFour), -1);
        check("degree-four minimum-height impossible",
                solution.findMinimumHeightRoot(5, degreeFour), -1);

        check("single node", solution.findRoot(1, new int[0][0]), 0);
        check("empty tree", solution.findRoot(0, new int[0][0]), -1);

        System.out.println("all passed");
    }

    private static void check(String testName, int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError(
                    "FAIL " + testName + ": got " + actual + " want " + expected);
        }

        System.out.println("pass " + testName + " -> " + actual);
    }
}