import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/*
 * ============================================================================
 * Problem 39 (Google L4 prep) — Sequence Reconstruction
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * `nums` is a target ordering. `sequences` is a list of subsequences, each of
 * which pins down some relative order ([a, b, c] means a before b before c).
 * Return true only if `nums` is the ONE AND ONLY ordering consistent with all
 * the constraints — not merely a valid one.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   A number appearing in `sequences` but not in `nums` makes reconstruction
 *   impossible -> false (rather than being silently ignored).
 *
 * EXAMPLES
 *   nums = [1,2,3], sequences = [[1,2],[2,3]]
 *     1->2->3 is forced -> true
 *
 *   nums = [1,2,3], sequences = [[1,2]]
 *     3 is unconstrained, so 1,2,3 / 1,3,2 / 3,1,2 all work -> false
 *
 *   nums = [1,2,3], sequences = [[1,2],[1,3]]
 *     after 1, both 2 and 3 are available -> false
 *
 *   nums = [4,1,5,2,6,3], sequences = [[5,2,6,3],[4,1,5,2]] -> true
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Story cue: "x must come before y" => DIRECTED GRAPH + TOPOLOGICAL SORT.
 * Each adjacent pair inside a subsequence becomes one edge from -> to.
 *
 * But the twist is UNIQUENESS, not mere validity. In Kahn's BFS the queue holds
 * every node with indegree 0 — i.e. every node we are FREE to place next. So:
 *
 *   queue.size() > 1  ->  a genuine choice exists  ->  >= 2 valid orderings
 *   queue.size() == 0 ->  nodes remain but all blocked  ->  a cycle
 *
 * THE CORE INVARIANT: at every position there must be EXACTLY ONE available
 * node, and it must equal nums[index]. That single line
 *
 *     if (queue.size() != 1) return false;
 *
 * is the whole problem. A unique topological order is exactly a Hamiltonian
 * path through the DAG, and the queue-size check detects that without ever
 * enumerating orderings.
 *
 * Edges must be DEDUPED: the same pair can appear in two subsequences, and
 * double-counting indegree would leave a node permanently blocked, turning a
 * true answer into a false one. `graph.get(from).add(to)` returning false is
 * the natural place to skip the increment.
 *
 * APPROACHES
 *   Brute force : generate every ordering satisfying the constraints and check
 *                 there is exactly one, equal to nums. O(N!) — hopeless.
 *   Optimal     : Kahn's BFS with the queue-size == 1 gate (below). One pass.
 *
 * COMPLEXITY
 *   Let N = nums.length, E = total unique ordering relationships.
 *   Time  O(N + E)
 *   Space O(N + E)
 * ----------------------------------------------------------------------------
 */
public class SequenceReconstruction {

    public boolean sequenceReconstruction(
            int[] nums,
            List<List<Integer>> sequences) {

        Map<Integer, Set<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> indegree = new HashMap<>();

        // Initialize all numbers from the target.
        for (int num : nums) {
            graph.put(num, new HashSet<>());
            indegree.put(num, 0);
        }

        // Build directed edges from every adjacent pair
        // appearing inside each subsequence.
        for (List<Integer> sequence : sequences) {

            for (int num : sequence) {
                if (!graph.containsKey(num)) {
                    return false;
                }
            }

            for (int i = 1; i < sequence.size(); i++) {

                int from = sequence.get(i - 1);
                int to = sequence.get(i);

                // Avoid counting the same edge twice.
                if (graph.get(from).add(to)) {
                    indegree.put(to, indegree.get(to) + 1);
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();

        // All currently available nodes.
        for (int num : nums) {
            if (indegree.get(num) == 0) {
                queue.offer(num);
            }
        }

        int index = 0;

        while (!queue.isEmpty()) {

            // More than one choice means multiple valid
            // topological orders exist.
            if (queue.size() != 1) {
                return false;
            }

            int current = queue.poll();

            // The unique order must exactly match nums.
            if (index >= nums.length || current != nums[index]) {
                return false;
            }

            index++;

            // Remove current from the graph.
            for (int neighbor : graph.get(current)) {

                indegree.put(
                    neighbor,
                    indegree.get(neighbor) - 1
                );

                if (indegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Every target element must have been reconstructed.
        return index == nums.length;
    }

    public static void main(String[] args) {
        SequenceReconstruction solution = new SequenceReconstruction();

        check("chain 1->2->3 forces a single ordering",
                solution.sequenceReconstruction(
                        new int[] { 1, 2, 3 },
                        List.of(List.of(1, 2), List.of(2, 3))),
                true);

        // 3 is unconstrained, so it can slide anywhere.
        check("unconstrained element leaves multiple orderings",
                solution.sequenceReconstruction(
                        new int[] { 1, 2, 3 },
                        List.of(List.of(1, 2))),
                false);

        // After 1, both 2 and 3 sit at indegree 0 -> a real choice.
        check("two nodes free at once",
                solution.sequenceReconstruction(
                        new int[] { 1, 2, 3 },
                        List.of(List.of(1, 2), List.of(1, 3))),
                false);

        check("overlapping subsequences pin the whole order",
                solution.sequenceReconstruction(
                        new int[] { 4, 1, 5, 2, 6, 3 },
                        List.of(List.of(5, 2, 6, 3), List.of(4, 1, 5, 2))),
                true);

        // 5->2 is stated twice; double-counting indegree would block 2 forever.
        check("duplicate edge must not be counted twice",
                solution.sequenceReconstruction(
                        new int[] { 1, 2 },
                        List.of(List.of(1, 2), List.of(1, 2))),
                true);

        // 1->2 and 2->1 -> nothing ever reaches indegree 0.
        check("cycle leaves the queue empty",
                solution.sequenceReconstruction(
                        new int[] { 1, 2 },
                        List.of(List.of(1, 2), List.of(2, 1))),
                false);

        check("number outside nums makes it unreconstructible",
                solution.sequenceReconstruction(
                        new int[] { 1, 2, 3 },
                        List.of(List.of(1, 2), List.of(2, 3), List.of(4))),
                false);

        check("valid order but not the target order",
                solution.sequenceReconstruction(
                        new int[] { 3, 2, 1 },
                        List.of(List.of(1, 2), List.of(2, 3))),
                false);

        check("single element",
                solution.sequenceReconstruction(
                        new int[] { 1 },
                        List.of(List.of(1))),
                true);

        System.out.println("all passed");
    }

    private static void check(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
