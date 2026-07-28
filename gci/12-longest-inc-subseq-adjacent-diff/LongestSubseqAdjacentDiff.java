/*
 * ============================================================================
 * Problem 12 (Google L4 prep) — Longest Increasing Subsequence with a Bounded
 *                               Adjacent Difference   [O(n) base + O(n^2) DP]
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given an integer array `nums`, find the LENGTH of the longest SUBSEQUENCE
 * (indices strictly increasing, elements need NOT be contiguous) such that every
 * consecutive pair in the subsequence increases by EXACTLY 1:
 *      nums[k+1] == nums[k] + 1.
 *
 *   Input : int[] nums
 *   Output: length of the longest such subsequence (>= 1 for a non-empty array).
 *
 * FOLLOW-UPS THIS HANDLES
 * -----------------------
 *   (A) Difference AT MOST D: relax the rule to  1 <= nums[k+1] - nums[k] <= D.
 *   (B) Reconstruct the actual subsequence (the path / indices), not just length.
 *
 * EXAMPLES
 * --------
 *   nums = [1,2,3,5,4,5,6]                 -> 6   (1,2,3,4,5,6 in index order)
 *   nums = [10,9,8]                        -> 1   (no increase possible)
 *   nums = [1,3,5,7],  D = 2  (follow-up)  -> 4   (1,3,5,7; each gap 2 <= D)
 *
 * ----------------------------------------------------------------------------
 * INTUITION
 * ----------------------------------------------------------------------------
 * Classic subsequence DP.  Define
 *      dp[i] = length of the longest valid chain ENDING at index i.
 * A chain ending at i either starts fresh (length 1) or extends some earlier
 * chain ending at an ALLOWED predecessor j (j < i and the step is legal):
 *      dp[i] = 1 + max{ dp[j] : j < i and step(nums[j], nums[i]) is allowed }.
 * The answer is max(dp).  We simply try every j < i for each i -> O(n^2).
 *
 *   - EXACT DIFF 1  : step allowed when nums[i] - nums[j] == 1.
 *   - DIFF AT MOST D: step allowed when 1 <= nums[i] - nums[j] <= D.
 *
 * PATH RECONSTRUCTION (follow-up B):
 *   parent[i] = the j that gave dp[i] its value (or -1 if it started fresh).
 *   Track the index with the global max dp; walk parents back and reverse.
 *
 * COMPLEXITY
 *   Base diff-1, OPTIMAL: lengthDiffOneOptimal (length) and
 *     indicesDiffOneOptimal (path indices) -> O(n) time / O(n) space
 *     (value-keyed DP; the only legal predecessor of value x is x - 1, so no
 *      inner scan is needed).
 *   diff<=D:
 *     lengthDiffAtMostDOptimal -> O(n log M) time / O(M) space (M = max value),
 *       segment tree keyed by VALUE: range-max over [value-D, value-1] +
 *       point-update at `value`. Assumes values >= 1 (LeetCode 2407).
 *     Path reconstruction (pathDiffAtMostD) stays O(n^2) time / O(n) space.
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class LongestSubseqAdjacentDiff {

    // ------------------------------------------------------------------
    // BASE: longest subsequence with adjacent difference EXACTLY 1 (length).
    // ------------------------------------------------------------------
    public int lengthDiffOne(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        int[] dp = new int[n];              // dp[i] = best chain length ending at i
        int ans = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;                      // chain of just nums[i]
            for (int j = 0; j < i; j++) {
                if (nums[i] - nums[j] == 1 && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }

    // ------------------------------------------------------------------
    // BASE, OPTIMAL O(n): longest subsequence with adjacent difference
    // EXACTLY 1. The only legal predecessor of a value x is x - 1, so instead
    // of scanning earlier indices we key the DP by VALUE:
    //      bestLen[x] = bestLen[x - 1] + 1
    // long keys avoid overflow when x == Integer.MIN_VALUE (x - 1 underflow).
    // ------------------------------------------------------------------
    public int lengthDiffOneOptimal(int[] nums) {
        Map<Long, Integer> bestLength = new HashMap<>();
        int max = 0;
        //bestLength(value) = bestLength(value - 1) + 1
        for(int num: nums)
        {
            long value = num;
            int bestPossible = bestLength.getOrDefault(value - 1, 0) + 1;
            bestLength.put(value, Math.max(bestLength.getOrDefault(value, 0), bestPossible));
            max = Math.max(max, bestLength.get(value));
        }

        return max;
    }

    // ------------------------------------------------------------------
    // BASE + PATH, OPTIMAL O(n): reconstruct the INDICES of the longest
    // subsequence with adjacent difference EXACTLY 1. long value-keys avoid
    // the Integer.MIN_VALUE - 1 underflow.
    // ------------------------------------------------------------------

    public List<Integer> indicesDiffOneOptimalV2(int[] nums) {
        int n = nums.length;
        if(n == 0)
        {
            return new ArrayList<>();
        }

        Map<Integer, Integer> bestValueIndex = new HashMap<>(); //value -> indice

        int[] parent = new int[n];
        int[] length = new int[n]; //gives best length ending at i
        Arrays.fill(parent, -1);

        for(int i = 0; i < n; i++)
        {
            int currentValue = nums[i];

            Integer previousIndex = bestValueIndex.get(currentValue - 1);
            if(previousIndex == null) //nothing exists before this value
            {
                length[i] = 1;
            }
            else
            {
                length[i] = length[previousIndex] + 1;
                parent[i] = previousIndex;
            }

            //check if currentValue have a previous best
            Integer currentValueBestIndex = bestValueIndex.get(currentValue);

            if(currentValueBestIndex == null 
                || length[i] > length[currentValueBestIndex])
            {
                bestValueIndex.put(currentValue, i);
            }
        }
        int bestendingIndex = -1;
        //for best index, loop through length to get it
        for(int i = 0; i < n; i++)
        {
            if(bestendingIndex == -1 || length[i] > length[bestendingIndex])
            {
                bestendingIndex = i;
            }
        }

        //now get the result
        List<Integer> path = new ArrayList<>();

        //start with bestendingIndex
        int index = bestendingIndex;
        while(index != -1)
        {
            path.add(index);
            index = parent[index];
        }

        Collections.reverse(path);
        return path;
    }
    public List<Integer> indicesDiffOneOptimal(int[] nums) {
        int n = nums.length;

        if (n == 0) {
            return new ArrayList<>();
        }

        /*
         * value -> index
         *
         * For each value, remember the index where the longest path
         * ending with that value currently finishes.
         *
         * Example:
         * nums = [2, 3]
         * bestIndexEndingWithValue:
         * 2 -> 0
         * 3 -> 1
         */
        Map<Long, Integer> bestIndexEndingWithValue = new HashMap<>();

        /*
         * pathLengthEndingAt[i]:
         * Length of the best path whose final element is nums[i].
         *
         * previousIndex[i]:
         * Index immediately before i in that path.
         * -1 means nums[i] starts the path.
         */
        int[] pathLengthEndingAt = new int[n];
        int[] previousIndex = new int[n];
        Arrays.fill(previousIndex, -1);

        /*
         * Index where the longest path found anywhere ends.
         * We start reconstruction from this index.
         */
        int longestPathEndIndex = 0;

        for (int currentIndex = 0; currentIndex < n; currentIndex++) {
            long currentValue = nums[currentIndex];

            /*
             * To place currentValue after another element,
             * the previous value must be currentValue - 1.
             * If currentValue = 4, search for the best path ending at 3.
             */
            Integer predecessorIndex = bestIndexEndingWithValue.get(currentValue - 1);

            if (predecessorIndex == null) {
                // No predecessor exists, so start a new path.
                pathLengthEndingAt[currentIndex] = 1;
            } else {
                // Extend the predecessor's path.
                pathLengthEndingAt[currentIndex] = pathLengthEndingAt[predecessorIndex] + 1;
                // Leave a breadcrumb for reconstruction.
                previousIndex[currentIndex] = predecessorIndex;
            }

            /*
             * Decide whether currentIndex is now the best ending index
             * for paths whose final value is currentValue.
             */
            Integer existingEndIndex = bestIndexEndingWithValue.get(currentValue);
            if (existingEndIndex == null
                    || pathLengthEndingAt[currentIndex] > pathLengthEndingAt[existingEndIndex]) {
                bestIndexEndingWithValue.put(currentValue, currentIndex);
            }

            // Remember where the longest path across all values ends.
            if (pathLengthEndingAt[currentIndex] > pathLengthEndingAt[longestPathEndIndex]) {
                longestPathEndIndex = currentIndex;
            }
        }

        /*
         * Walk backward through the breadcrumbs, then reverse.
         * Example: 6 -> 5 -> 3 -> 1 -> 0
         */
        List<Integer> resultIndices = new ArrayList<>();
        int currentIndex = longestPathEndIndex;
        while (currentIndex != -1) {
            resultIndices.add(currentIndex);
            currentIndex = previousIndex[currentIndex];
        }
        Collections.reverse(resultIndices);

        return resultIndices;
    }

    // ------------------------------------------------------------------
    // BASE + PATH: reconstruct the actual subsequence (values), diff EXACTLY 1.
    // ------------------------------------------------------------------
    public List<Integer> pathDiffOne(int[] nums) {
        return solve(nums, 1);              // D = 1 means the only step is +1
    }

    // ------------------------------------------------------------------
    // FOLLOW-UP A + B: longest subsequence with 1 <= adjacent diff <= D,
    //                  returning the reconstructed subsequence (values).
    // ------------------------------------------------------------------
    public List<Integer> pathDiffAtMostD(int[] nums, int D) {
        return solve(nums, D);
    }

    // ------------------------------------------------------------------
    // Shared O(n^2) DP + path reconstruction.
    // A step j -> i is allowed when  1 <= nums[i] - nums[j] <= D.
    // (D = 1 gives the "exactly 1" base problem.)
    // ------------------------------------------------------------------
    private List<Integer> solve(int[] nums, int D) {
        int n = nums.length;
        if (n == 0) return new ArrayList<>();

        int[] dp = new int[n];              // dp[i] = best chain length ending at i
        int[] parent = new int[n];          // predecessor index for dp[i], or -1
        Arrays.fill(parent, -1);

        int globalLen = 0, globalEnd = -1;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                if (diff >= 1 && diff <= D && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > globalLen) {
                globalLen = dp[i];
                globalEnd = i;
            }
        }

        // Walk parent pointers back from the best endpoint, then reverse.
        LinkedList<Integer> path = new LinkedList<>();
        for (int i = globalEnd; i != -1; i = parent[i]) {
            path.addFirst(nums[i]);
        }
        return path;
    }

    // ------------------------------------------------------------------
    // FOLLOW-UP A, OPTIMAL O(n log M): LENGTH of the longest strictly-
    // increasing subsequence with 1 <= adjacent diff <= D  (LeetCode 2407).
    //
    // Value-keyed DP: bestLength[value] = longest chain ending EXACTLY at value.
    // To append `value`, the previous value must lie in [value - D, value - 1]
    // (upper bound value-1 keeps it STRICTLY increasing). A segment tree over
    // the value axis answers that range-max window and the point-update at
    // `value` in O(log M). Assumes values >= 1 (LeetCode: 1 <= nums[i] <= 1e5).
    // ------------------------------------------------------------------
    public int lengthDiffAtMostDOptimal(int[] nums, int D) {
        // Phase 1: edge cases.
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (D <= 0) {
            return 1;               // no adjacent step is legal -> a single element is best
        }

        // Phase 2: size the tree. Positions ARE the numeric values, so size = max value.
        int maxValue = 0;
        for (int value : nums) {
            maxValue = Math.max(maxValue, value);
        }

        SegmentTree tree = new SegmentTree(maxValue);
        int longest = 1;

        // Phase 3: core DP. Process values left-to-right so predecessors are
        // exactly the elements already inserted (original order preserved).
        for (int value : nums) {
            // Legal predecessors live in [value - D, value - 1].
            int left = Math.max(1, value - D);
            int right = value - 1;

            int bestPrevious = tree.query(left, right);
            int current = bestPrevious + 1;

            // Duplicate values: keep the best chain ending exactly at `value`.
            tree.update(value, current);
            longest = Math.max(longest, current);
        }

        return longest;
    }

    // ------------------------------------------------------------------
    // Range-max segment tree over the value axis (1..size). O(log size) per op.
    //   tree[node] = max DP length stored in that node's value range.
    // ------------------------------------------------------------------
    private static class SegmentTree {
        private final int[] tree;
        private final int size;

        SegmentTree(int size) {
            this.size = size;
            this.tree = new int[4 * size];
        }

        // Max DP length stored in values [queryLeft, queryRight] (0 if empty).
        int query(int queryLeft, int queryRight) {
            if (queryLeft > queryRight) {
                return 0;
            }
            return query(1, 1, size, queryLeft, queryRight);
        }

        private int query(int node, int segLeft, int segRight, int qLeft, int qRight) {
            if (qRight < segLeft || segRight < qLeft) {      // no overlap
                return 0;
            }
            if (qLeft <= segLeft && segRight <= qRight) {    // full overlap
                return tree[node];
            }
            int mid = segLeft + (segRight - segLeft) / 2;
            return Math.max(
                    query(node * 2, segLeft, mid, qLeft, qRight),
                    query(node * 2 + 1, mid + 1, segRight, qLeft, qRight));
        }

        // Point update: best length ending exactly at `value`.
        void update(int value, int newLength) {
            update(1, 1, size, value, newLength);
        }

        private void update(int node, int segLeft, int segRight, int value, int newLength) {
            if (segLeft == segRight) {
                tree[node] = Math.max(tree[node], newLength);
                return;
            }
            int mid = segLeft + (segRight - segLeft) / 2;
            if (value <= mid) {
                update(node * 2, segLeft, mid, value, newLength);
            } else {
                update(node * 2 + 1, mid + 1, segRight, value, newLength);
            }
            tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);   // restore range-max
        }
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        LongestSubseqAdjacentDiff sol = new LongestSubseqAdjacentDiff();

        int[] a = {1, 2, 3, 5, 4, 5, 6};
        System.out.println(sol.lengthDiffOne(a));        // 6
        System.out.println(sol.lengthDiffOneOptimal(a)); // 6  (O(n) value-keyed)
        System.out.println(sol.pathDiffOne(a));          // [1, 2, 3, 4, 5, 6]

        int[] b = {10, 9, 8};
        System.out.println(sol.lengthDiffOne(b));        // 1
        System.out.println(sol.pathDiffOne(b));          // [10] (any single element)

        int[] c = {1, 3, 5, 7};
        System.out.println(sol.pathDiffAtMostD(c, 2));   // [1, 3, 5, 7]
        System.out.println(sol.pathDiffAtMostD(a, 1));   // [1, 2, 3, 4, 5, 6]

        int[] d = {2, 3, 1, 4, 3, 5, 6};
        System.out.println(sol.indicesDiffOneOptimal(d));   // [0, 1, 3, 5, 6]
        System.out.println(sol.indicesDiffOneOptimalV2(d)); // [0, 1, 3, 5, 6]

        int[] e = {5, 6, 7, 1, 2, 3};                        // two runs of len 3; earliest wins
        System.out.println(sol.indicesDiffOneOptimal(e));   // [0, 1, 2]  (values 5,6,7)
        System.out.println(sol.indicesDiffOneOptimalV2(e)); // [0, 1, 2]  (values 5,6,7)

        int[] f = {4, 4, 5, 6};                              // duplicate 4; still values 4,5,6
        System.out.println(sol.indicesDiffOneOptimal(f));   // [0, 2, 3]  (values 4,5,6)
        System.out.println(sol.indicesDiffOneOptimalV2(f)); // [0, 2, 3]  (values 4,5,6)

        int[] g = {4, 2, 1, 4, 3, 4, 5, 8, 15};              // diff<=D, O(n log M) segment tree
        System.out.println(sol.lengthDiffAtMostDOptimal(g, 3)); // 5  (1,3,4,5,8)
    }
}
