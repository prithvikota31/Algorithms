import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Set;

/*
 * ============================================================================
 * Problem 33 (Google L4 prep) — Shortest Path Through Broken Teleporters
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Teleporters are directed graph nodes. Some are BROKEN and cannot be used
 * at all. Return the ACTUAL shortest path (as a list of teleporters, not
 * just its length) from source to destination, using only working
 * teleporters.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   A broken teleporter can never appear in the route — if source or
 *   destination itself is broken, return an empty list. `source == destination`
 *   is a valid (trivial, zero-edge) path.
 *
 * EXAMPLE
 *   0 -> 1 -> 3 -> 5
 *    \-> 2 -> 4 -/
 *   broken = {3}, source=0, destination=5
 *   [0,1,3,5] is invalid (uses broken 3)  ->  answer = [0,2,4,5]
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: UNWEIGHTED SHORTEST PATH = BFS + PARENT-POINTER RECONSTRUCTION.
 *
 * BFS explores nodes strictly in order of edge-count from the source, so the
 * FIRST time a node is discovered, that discovery is via a shortest path.
 * Track `parent[node]` = the node BFS discovered it from; once the
 * destination is reached, walk `parent` backward (destination -> ... ->
 * source) and reverse to get source -> ... -> destination.
 *
 * Broken teleporters are simply never added to the queue — they act like
 * they don't exist in the graph at all.
 *
 * APPROACHES
 *   Brute force : enumerate every simple path, keep the shortest valid one.
 *                 Exponential time on graphs with cycles/many paths.
 *   Optimal     : BFS with parent pointers (below). O(V + E) time.
 *
 * COMPLEXITY
 *   Time O(V + E)   Space O(V)
 * ----------------------------------------------------------------------------
 */
public class TeleporterShortestPath {

    public List<Integer> findShortestPath(int numberOfTeleporters, List<List<Integer>> connections,
            Set<Integer> brokenTeleporters, int source, int destination) {
        if(brokenTeleporters.contains(source) || brokenTeleporters.contains(destination))
        {
            return new ArrayList<>();
        }
        if(source == destination)
        {
            return Arrays.asList(source);
        }
        int[] distance = new int[numberOfTeleporters];

        Arrays.fill(distance, -1);
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(source);
        int[] parent = new int[numberOfTeleporters];
        parent[source] = source;
        distance[source] = 0;
        

        while(!q.isEmpty())
        {
            int cur = q.poll();

            if(cur == destination)
            {
                break;
            }
            for(int nei: connections.get(cur))
            {
                if(distance[nei] == -1 && !brokenTeleporters.contains(nei))
                {
                    parent[nei] = cur;
                    distance[nei] = distance[cur] + 1;
                    q.offer(nei);
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        if(distance[destination] == -1)
        {
            return result;
        }

        int cur = destination;
        while(parent[cur] != cur)
        {
            result.add(cur);
            cur = parent[cur];
        }
        result.add(source);
        Collections.reverse(result);
        return result;

        
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): teleporters can be WORKING (free to leave) or
     * PARTIALLY_REPAIRED (costs 1 repair-day to leave); BROKEN is still fully
     * unusable. Minimize total repair days on the route.
     *
     * MENTAL MAP
     *   Edge weights are now only 0 or 1 (the departure cost of the CURRENT
     *   node, applied to every edge leaving it) — this is the classic 0-1 BFS
     *   setup. A plain queue no longer works: a 0-cost edge must be explored
     *   BEFORE a 1-cost edge discovered earlier, or we might finalize a node
     *   too early with a worse distance.
     *
     *   Fix: use a DEQUE instead of a queue.
     *     - 0-cost relaxation -> push to the FRONT (explore it next, same
     *       "layer" as current).
     *     - 1-cost relaxation -> push to the BACK (explore it only after
     *       every current-layer node is settled).
     *   This keeps the deque ordered by distance at all times, giving
     *   Dijkstra-correct results without a priority queue's O(log V) cost.
     * ------------------------------------------------------------------------
     */
    public enum Status { WORKING, PARTIALLY_REPAIRED, BROKEN }

    public List<Integer> findMinRepairDaysPath(int numberOfTeleporters, List<List<Integer>> graph,
            Status[] status, int source, int destination) {
        //first verify if status of src and dst is broken
        List<Integer> result = new ArrayList<>();
        if(status[source] == Status.BROKEN || status[destination] == Status.BROKEN)
        {
            return result;
        }

        //check is source equals destionation
        if(source == destination)
        {
            result.add(source);
            return result;
        }

        int[] distance = new int[numberOfTeleporters];
        int[] parent = new int[numberOfTeleporters];
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        parent[source] = source;
        distance[source] = 0;
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(source);
        while(!q.isEmpty())
        {
            int cur = q.poll();
            if(cur == destination)
            {
                break;
            }
            int costToRepair = 0;
            if(status[cur] == Status.PARTIALLY_REPAIRED)
            {
                costToRepair = 1;
            }
            

            for(int nei: graph.get(cur))
            {
                if(status[nei] != Status.BROKEN && distance[cur] + costToRepair < distance[nei])
                {
                    parent[nei] = cur;
                    distance[nei] = distance[cur] + costToRepair;
                    if(costToRepair == 1)
                    {
                        //put behind the queue
                        q.offerLast(nei);
                    }
                    else
                    {
                        q.offerFirst(nei);
                    }
                }
            }
        }

        //add parents in the result from destination
        if(parent[destination] == -1)
        {
            return result;
        }
        int cur = destination;
        while(parent[cur] != cur)
        {
            result.add(cur);
            cur = parent[cur];
        }
        result.add(cur);
        Collections.reverse(result);
        return result;
    }

    public static void main(String[] args) {
        TeleporterShortestPath solution = new TeleporterShortestPath();

        List<List<Integer>> connections = List.of(
                List.of(1, 2),
                List.of(3),
                List.of(4),
                List.of(5),
                List.of(5),
                List.of());
        Set<Integer> broken = Set.of(3);

        check("worked example", solution.findShortestPath(6, connections, broken, 0, 5),
                List.of(0, 2, 4, 5));
        check("source equals destination", solution.findShortestPath(6, connections, broken, 2, 2),
                List.of(2));
        check("broken destination returns empty", solution.findShortestPath(6, connections, broken, 0, 3),
                List.of());
        check("unreachable destination returns empty",
                solution.findShortestPath(6, List.of(List.of(), List.of(), List.of(), List.of(), List.of(), List.of()),
                        Set.of(), 0, 5),
                List.of());

        // Follow-up: a 3-hop all-WORKING route beats a 2-hop route through a
        // PARTIALLY_REPAIRED teleporter, since total repair days matter, not
        // hop count.
        List<List<Integer>> repairConnections = List.of(
                List.of(1, 2), // 0
                List.of(4),    // 1
                List.of(3),    // 2
                List.of(4),    // 3
                List.of());    // 4
        TeleporterShortestPath.Status[] status = {
                TeleporterShortestPath.Status.WORKING,             // 0
                TeleporterShortestPath.Status.PARTIALLY_REPAIRED,  // 1
                TeleporterShortestPath.Status.WORKING,             // 2
                TeleporterShortestPath.Status.WORKING,             // 3
                TeleporterShortestPath.Status.WORKING              // 4
        };
        check("cheaper longer route beats costlier shorter route",
                solution.findMinRepairDaysPath(5, repairConnections, status, 0, 4),
                List.of(0, 2, 3, 4));

        System.out.println("all passed");
    }

    private static void check(String name, List<Integer> actual, List<Integer> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
