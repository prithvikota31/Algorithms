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
 *   diff<=D and its path reconstruction: O(n^2) time / O(n) space.
 *     (An O(n log n) segment-tree range-max speed-up for diff<=D exists but is
 *      intentionally not used here.)
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
    // subsequence with adjacent difference EXACTLY 1.
    //   bestEndIndex[value] = index where the best chain ending at `value` ends
    //   parent[i]           = index used just before i (breadcrumb), or -1
    // The map finds the best previous index (value - 1); parent[] leaves the
    // trail back to it. long value-keys avoid the Integer.MIN_VALUE - 1 underflow.
    // ------------------------------------------------------------------
    public List<Integer> indicesDiffOneOptimal(int[] nums) {
        int n = nums.length;
        Map<Long, Integer> bestEndIndex = new HashMap<>();

        int[] length = new int[n];          // length[i] = best chain length ending at i
        int[] parent = new int[n];          // predecessor index for i, or -1
        Arrays.fill(parent, -1);

        int bestOverallEnd = -1;

        for (int i = 0; i < n; i++) {
            long value = nums[i];

            // Best earlier chain ends at value - 1 (the only legal predecessor).
            Integer previousIndex = bestEndIndex.get(value - 1);
            if (previousIndex == null) {
                length[i] = 1;
            } else {
                length[i] = length[previousIndex] + 1;
                parent[i] = previousIndex;
            }

            // Keep i as the representative end for `value` only if it's better.
            Integer existingEnd = bestEndIndex.get(value);
            if (existingEnd == null || length[i] > length[existingEnd]) {
                bestEndIndex.put(value, i);
            }

            if (bestOverallEnd == -1 || length[i] > length[bestOverallEnd]) {
                bestOverallEnd = i;
            }
        }

        // Follow parent breadcrumbs backward, prepending to get index order.
        LinkedList<Integer> indices = new LinkedList<>();
        for (int i = bestOverallEnd; i != -1; i = parent[i]) {
            indices.addFirst(i);
        }
        return indices;
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
        System.out.println(sol.indicesDiffOneOptimal(d)); // [0, 1, 3, 5, 6]
    }
}
