import java.util.*;

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

        int n = nums.length;
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        for(int i = 0; i < n; i++)
        {
            graph.put(nums[i], new HashSet<>());
            inDegree.put(nums[i], 0);
        }


        for(int i = 0; i < sequences.size(); i++)
        {
            List<Integer> sequence = sequences.get(i);

            for (int value : sequence) {
                if (!graph.containsKey(value)) {
                    return false;
                }
            }
            for(int j = 0; j <= sequence.size() - 2; j++)
            {
                int u = sequence.get(j);
                int v = sequence.get(j + 1);

                if(!graph.containsKey(u) || !graph.containsKey(v))
                {
                    return false;        
                }

                boolean updateInDegree = graph.get(u).add(v);
                if(updateInDegree)
                {
                    inDegree.put(v, inDegree.get(v) + 1);
                }
            }
        }

        Deque<Integer> q = new ArrayDeque<>();
        for(int key: inDegree.keySet())
        {
            if(inDegree.get(key) == 0)
            {
                q.offer(key);
            }
        }

        if(q.size() != 1)   return false;
        int index = 0;
        while(!q.isEmpty())
        {
            if(q.size() != 1)   return false;
            int cur = q.poll();

            if(nums[index] != cur)
            {
                return false;
            }
            index++;
            
            for(int nei: graph.get(cur))
            {
                inDegree.put(nei, inDegree.get(nei) - 1);

                if(inDegree.get(nei) == 0)
                {
                    q.offer(nei);
                    inDegree.remove(nei);
                }
            }
        }

        return index == n;      
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
