import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/*
 * ============================================================================
 * Problem 35 (Google L4 prep) — Broadcast Signal Propagation (Max Reach)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Each transmitter is [x, y, radius]. If transmitter A's radius reaches
 * transmitter B's location, activating A also activates B, which can then
 * activate ITS reachable transmitters, and so on. Given exactly one initial
 * transmitter to activate, return the MAXIMUM number of transmitters that
 * can end up active, over every possible choice of starting transmitter.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   Reachability is DIRECTED: A reaching B (B is within A's radius) does NOT
 *   imply B reaches A (different radii). "A can reach B" is purely a
 *   geometric distance <= radius check, independent of whether B is already
 *   active.
 *
 * EXAMPLE
 *   transmitters = [[2,1,3], [6,1,4]]
 *   0 cannot reach 1 (distance 4 > radius 3), but 1 CAN reach 0 (distance 4
 *   <= radius 4). Starting from 0 activates 1 transmitter; starting from 1
 *   activates 2 (itself, then 0).  -> answer = 2
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: BUILD A DIRECTED REACHABILITY GRAPH, THEN DFS/BFS FROM EVERY NODE.
 *
 * Once "i -> j" edges are built from the geometry (distance(i,j) <=
 * radius[i]), the geometry is irrelevant from then on — the problem becomes
 * plain graph traversal: "starting from each node, how many nodes are
 * reachable?" Take the max over all starting nodes.
 *
 * Use SQUARED distance (dx*dx + dy*dy <= radius*radius) to avoid floating-
 * point sqrt() entirely — use `long` for the squaring since coordinates and
 * radii can overflow `int` once squared.
 *
 * APPROACHES
 *   Brute force : re-derive reachability geometrically on every step of the
 *                 simulation instead of precomputing edges once. Mixes
 *                 geometry with traversal and repeats distance checks.
 *   Optimal     : build the directed graph once (O(n^2) pairs), then run
 *                 DFS from every node (below).
 *
 * COMPLEXITY
 *   Time O(n^2) to build the graph + O(n^2) worst-case edges times n starts
 *        = O(n^3) worst case (fine for typical n <= 100 constraints)
 *   Space O(n^2) for the graph
 * ----------------------------------------------------------------------------
 */
public class BroadcastSignalPropagation {

    public int maximumReach(int[][] transmitters) {
        int n = transmitters.length;
        if (n == 0) {
            return 0;
        }

        List<List<Integer>> graph = buildReachabilityGraph(transmitters);

        int maxReached = 0;
        for (int start = 0; start < n; start++) {
            boolean[] visited = new boolean[n];
            maxReached = Math.max(maxReached, dfsCount(start, graph, visited));
        }
        return maxReached;
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): given a SPECIFIC starting transmitter, return
     * the actual set of transmitters that become active (not just the count).
     *
     * MENTAL MAP
     *   Same directed graph, same DFS — just collect the visited nodes in
     *   the order discovered instead of only counting them.
     * ------------------------------------------------------------------------
     */
    public List<Integer> reachableTransmitters(int[][] transmitters, int start) {
        List<Integer> reached = new ArrayList<>();
        int n = transmitters.length;
        if (n == 0 || start < 0 || start >= n) {
            return reached;
        }

        List<List<Integer>> graph = buildReachabilityGraph(transmitters);
        boolean[] visited = new boolean[n];
        dfsCollect(start, graph, visited, reached);
        return reached;
    }

    private List<List<Integer>> buildReachabilityGraph(int[][] transmitters) {
        int n = transmitters.length;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < n; i++) {
            long x1 = transmitters[i][0];
            long y1 = transmitters[i][1];
            long radius = transmitters[i][2];

            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }

                long dx = x1 - transmitters[j][0];
                long dy = y1 - transmitters[j][1];
                long distanceSquared = dx * dx + dy * dy;

                if (distanceSquared <= radius * radius) {
                    graph.get(i).add(j);
                }
            }
        }

        return graph;
    }

    private int dfsCount(int current, List<List<Integer>> graph, boolean[] visited) {
        visited[current] = true;
        int count = 1;
        for (int neighbor : graph.get(current)) {
            if (!visited[neighbor]) {
                count += dfsCount(neighbor, graph, visited);
            }
        }
        return count;
    }

    private void dfsCollect(int current, List<List<Integer>> graph, boolean[] visited,
            List<Integer> reached) {
        visited[current] = true;
        reached.add(current);
        for (int neighbor : graph.get(current)) {
            if (!visited[neighbor]) {
                dfsCollect(neighbor, graph, visited, reached);
            }
        }
    }

    /*
     * ALTERNATIVE (equivalent to dfsCount/dfsCollect above): BFS with an
     * explicit queue instead of the recursion stack. `visited` must be set
     * at ENQUEUE time (not dequeue time) to avoid pushing the same node
     * twice; a FIFO queue's dequeue order always matches its enqueue order,
     * so counting/collecting at either point gives identical results.
     */
    public int maximumReachBfs(int[][] transmitters) {
        int n = transmitters.length;
        if (n == 0) {
            return 0;
        }

        List<List<Integer>> graph = buildReachabilityGraph(transmitters);

        int maxReached = 0;
        for (int start = 0; start < n; start++) {
            boolean[] visited = new boolean[n];
            maxReached = Math.max(maxReached, bfsCount(start, graph, visited));
        }
        return maxReached;
    }

    public List<Integer> reachableTransmittersBfs(int[][] transmitters, int start) {
        List<Integer> reached = new ArrayList<>();
        int n = transmitters.length;
        if (n == 0 || start < 0 || start >= n) {
            return reached;
        }

        List<List<Integer>> graph = buildReachabilityGraph(transmitters);
        boolean[] visited = new boolean[n];
        bfsCollect(start, graph, visited, reached);
        return reached;
    }

    private int bfsCount(int start, List<List<Integer>> graph, boolean[] visited) {
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        visited[start] = true;

        int count = 0;
        while (!queue.isEmpty()) {
            int current = queue.poll();
            count++;
            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        return count;
    }

    private void bfsCollect(int start, List<List<Integer>> graph, boolean[] visited,
            List<Integer> reached) {
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            reached.add(current);
            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
    }

    public static void main(String[] args) {
        BroadcastSignalPropagation solution = new BroadcastSignalPropagation();

        int[][] worked = { { 2, 1, 3 }, { 6, 1, 4 } };
        check("worked example", solution.maximumReach(worked), 2);

        // Directed chain: 0 -> 1 -> 2 (built so no reverse edges exist).
        int[][] chain = { { 0, 0, 5 }, { 5, 0, 4 }, { 9, 0, 0 } };
        check("directed chain reaches all three", solution.maximumReach(chain), 3);
        check("chain reachable set from 0", solution.reachableTransmitters(chain, 0), List.of(0, 1, 2));
        check("chain reachable set from 2 (dead end)",
                solution.reachableTransmitters(chain, 2), List.of(2));

        check("single transmitter reaches only itself",
                solution.maximumReach(new int[][] { { 0, 0, 0 } }), 1);
        check("empty transmitters", solution.maximumReach(new int[][] {}), 0);

        // BFS variants must match the DFS versions exactly (same graph, same reachability).
        check("worked example (BFS)", solution.maximumReachBfs(worked), 2);
        check("directed chain reaches all three (BFS)", solution.maximumReachBfs(chain), 3);
        check("chain reachable set from 0 (BFS)",
                solution.reachableTransmittersBfs(chain, 0), List.of(0, 1, 2));
        check("chain reachable set from 2 (BFS, dead end)",
                solution.reachableTransmittersBfs(chain, 2), List.of(2));
        check("single transmitter reaches only itself (BFS)",
                solution.maximumReachBfs(new int[][] { { 0, 0, 0 } }), 1);
        check("empty transmitters (BFS)", solution.maximumReachBfs(new int[][] {}), 0);

        System.out.println("all passed");
    }

    private static void check(String name, int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }

    private static void check(String name, List<Integer> actual, List<Integer> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
