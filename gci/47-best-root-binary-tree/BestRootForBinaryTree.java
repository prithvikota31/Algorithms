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
import java.util.List;
import java.util.Queue;

public class BestRootForBinaryTree {

    public int findRoot(int nodeCount, int[][] edges) {
        if (nodeCount == 0) {
            return -1;
        }

        int[] degree = degrees(nodeCount, edges);

        for (int node = 0; node < nodeCount; node++) {
            if (degree[node] > 3) {
                return -1;
            }
        }

        for (int node = 0; node < nodeCount; node++) {
            if (degree[node] <= 2) {
                return node;
            }
        }

        return -1;
    }

    /*
     * Follow-up: among valid roots, minimize the resulting tree height.
     * A node's height as root is its maximum distance to either endpoint of
     * the tree's diameter. Compute those two distance arrays, then choose the
     * valid root with minimum maximum distance.
     */
    public int findMinimumHeightRoot(int nodeCount, int[][] edges) {
        if (nodeCount == 0) {
            return -1;
        }

        List<List<Integer>> graph = buildGraph(nodeCount, edges);

        for (int node = 0; node < nodeCount; node++) {
            if (graph.get(node).size() > 3) {
                return -1;
            }
        }

        BfsResult firstSearch = bfs(graph, 0);
        BfsResult fromFirstEndpoint = bfs(graph, firstSearch.farthestNode);
        BfsResult fromSecondEndpoint = bfs(graph, fromFirstEndpoint.farthestNode);

        int bestRoot = -1;
        int minimumHeight = Integer.MAX_VALUE;

        for (int node = 0; node < nodeCount; node++) {
            if (graph.get(node).size() > 2) {
                continue;
            }

            int height = Math.max(
                    fromFirstEndpoint.distance[node],
                    fromSecondEndpoint.distance[node]);

            if (height < minimumHeight) {
                minimumHeight = height;
                bestRoot = node;
            }
        }

        return bestRoot;
    }

    private int[] degrees(int nodeCount, int[][] edges) {
        int[] degree = new int[nodeCount];

        for (int[] edge : edges) {
            degree[edge[0]]++;
            degree[edge[1]]++;
        }

        return degree;
    }

    private List<List<Integer>> buildGraph(int nodeCount, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();

        for (int node = 0; node < nodeCount; node++) {
            graph.add(new ArrayList<>());
        }

        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        return graph;
    }

    private BfsResult bfs(List<List<Integer>> graph, int start) {
        int[] distance = new int[graph.size()];
        Arrays.fill(distance, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        distance[start] = 0;
        int farthestNode = start;

        while (!queue.isEmpty()) {
            int node = queue.poll();

            if (distance[node] > distance[farthestNode]) {
                farthestNode = node;
            }

            for (int neighbor : graph.get(node)) {
                if (distance[neighbor] != -1) {
                    continue;
                }

                distance[neighbor] = distance[node] + 1;
                queue.offer(neighbor);
            }
        }

        return new BfsResult(farthestNode, distance);
    }

    private static class BfsResult {
        final int farthestNode;
        final int[] distance;

        BfsResult(int farthestNode, int[] distance) {
            this.farthestNode = farthestNode;
            this.distance = distance;
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