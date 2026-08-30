import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/*
 * ============================================================================
 * Problem 32 (Google L4 prep) — Movie Similarity Graph: Top N Reachable
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Each movie is a node in a similarity graph (edge = "these two are similar")
 * and has a rating. Given a starting movie and N, return the N highest-rated
 * movies REACHABLE from the start, highest rating first.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   Only movies reachable from `start` count — an unreachable movie with a
 *   higher rating must NOT appear in the result. The graph may contain
 *   cycles, so a visited set is required (not optional) just for correct
 *   termination, independent of the top-N logic.
 *
 * EXAMPLE
 *   A(8.5)--B(9.0)
 *     |
 *   C(7.5)--D(9.5)
 *   start=A, N=2  ->  [D(9.5), B(9.0)]
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: GRAPH TRAVERSAL + TOP-K MIN-HEAP.
 *
 * Whenever the question is "find the best N items among everything
 * reachable", the shape is always: (1) traverse to enumerate candidates,
 * (2) never store more than N of them at once via a min-heap.
 *
 *   BFS/DFS from `start`, visited-set guards against cycles/revisits.
 *   For every node visited: push it onto a MIN-heap of size N (ordered by
 *   rating). If the heap grows past N, pop the smallest — it can never be
 *   in the final top-N once N better candidates exist.
 *
 * At the end the heap holds exactly the top N reachable ratings, but in
 * ASCENDING order (min-heap draining always removes the smallest first) —
 * reverse the drained list to get highest-rated first.
 *
 * APPROACHES
 *   Brute force : collect every reachable movie, sort all of them, take the
 *                 top N. O(V log V) time.
 *   Optimal     : BFS/DFS + a size-capped min-heap (below). O(V + E) for the
 *                 traversal, O(log N) per heap op -> O((V+E) + V log N).
 *
 * COMPLEXITY
 *   Time O(V + E + V log N)   Space O(V + N)
 * ----------------------------------------------------------------------------
 */
public class TopNSimilarMovies {

    public List<String> topNMovies(String start, Map<String, Double> ratings,
            Map<String, List<String>> adjacency, int n) {
        List<String> result = new ArrayList<>();
        if (start == null || ratings == null || adjacency == null || n <= 0
                || !ratings.containsKey(start)) {
            return result;
        }

        Deque<String> q = new ArrayDeque<>();
        PriorityQueue<String> minHeap = 
                    new PriorityQueue<>((a, b) -> Double.compare(ratings.get(a), ratings.get(b)));
        
        Set<String> visited = new HashSet<>();
        q.offer(start);
        visited.add(start);
        while(!q.isEmpty())
        {
            String current  = q.poll();
            minHeap.offer(current);
            if(minHeap.size() > n)
            {
                minHeap.poll();
            }

            for(String nei: adjacency.getOrDefault(current, new ArrayList<>()))
            {
                if(!visited.contains(nei))
                {
                    visited.add(nei);
                    q.offer(nei);
                }
            }
        }

        while(!minHeap.isEmpty())
        {
            result.add(minHeap.poll());
        }

        Collections.reverse(result);
        return result;
    }

    public static void main(String[] args) {
        TopNSimilarMovies solution = new TopNSimilarMovies();

        Map<String, Double> ratings = Map.of("A", 8.5, "B", 9.0, "C", 7.5, "D", 9.5);
        Map<String, List<String>> adjacency = Map.of(
                "A", List.of("B", "C"),
                "B", List.of("A"),
                "C", List.of("A", "D"),
                "D", List.of("C"));

        check("worked example", solution.topNMovies("A", ratings, adjacency, 2), List.of("D", "B"));
        check("N covers every reachable movie",
                solution.topNMovies("A", ratings, adjacency, 10), List.of("D", "B", "A", "C"));

        Map<String, Double> ratingsWithUnreachable = Map.of(
                "A", 8.5, "B", 9.0, "C", 7.5, "D", 9.5, "E", 10.0);
        check("unreachable higher-rated movie excluded",
                solution.topNMovies("A", ratingsWithUnreachable, adjacency, 10),
                List.of("D", "B", "A", "C"));

        Map<String, Double> cycleRatings = Map.of("A", 1.0, "B", 2.0, "C", 3.0);
        Map<String, List<String>> cycleAdjacency = Map.of(
                "A", List.of("B", "C"),
                "B", List.of("A", "C"),
                "C", List.of("A", "B"));
        check("cycle handled without infinite loop",
                solution.topNMovies("A", cycleRatings, cycleAdjacency, 2), List.of("C", "B"));

        Map<String, Double> singleNodeRatings = Map.of("A", 5.0);
        check("single node, no neighbors",
                solution.topNMovies("A", singleNodeRatings, Map.of(), 3), List.of("A"));

        check("start not in ratings returns empty",
                solution.topNMovies("Z", ratings, adjacency, 2), List.of());

        System.out.println("all passed");
    }

    private static void check(String name, List<String> actual, List<String> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
