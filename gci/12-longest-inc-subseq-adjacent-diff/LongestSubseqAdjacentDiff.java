/*
 * ============================================================================
 * Problem 14 (Google L4 prep) — Longest Increasing Subsequence with a Bounded
 *                               Adjacent Difference   [plain O(n^2) DP version]
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
 * COMPLEXITY  (all methods)
 *   Time O(n^2)   Space O(n)   (n = nums.length)
 *   (The O(n) / O(n log n) speed-ups exist but are intentionally not used here.)
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
        System.out.println(sol.pathDiffOne(a));          // [1, 2, 3, 4, 5, 6]

        int[] b = {10, 9, 8};
        System.out.println(sol.lengthDiffOne(b));        // 1
        System.out.println(sol.pathDiffOne(b));          // [10] (any single element)

        int[] c = {1, 3, 5, 7};
        System.out.println(sol.pathDiffAtMostD(c, 2));   // [1, 3, 5, 7]
        System.out.println(sol.pathDiffAtMostD(a, 1));   // [1, 2, 3, 4, 5, 6]
    }
}
