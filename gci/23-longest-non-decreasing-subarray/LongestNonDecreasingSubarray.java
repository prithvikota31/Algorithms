/*
 * ============================================================================
 * Problem 23 (Google L4 prep) — Longest Non-Decreasing Contiguous Subarray
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Find the length of the longest CONTIGUOUS segment where every value is at
 * least the previous value (non-decreasing). Equal neighbours are allowed.
 *
 * Reported Google follow-up (NOT solved here): modify at most one index to any
 * integer, then maximize the length. LeetCode later formalized this as 3738.
 * We solve only the base problem now.
 *
 * EXAMPLE
 *   nums = [1, 2, 2, 1, 3, 4]
 *   Non-decreasing runs: [1,2,2] (len 3) and [1,3,4] (len 3)  ->  answer 3
 *   Equal values count because this is non-decreasing, not strictly increasing.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: ONE-PASS contiguous-run tracking. Keep the start of the current
 * valid run.
 *
 *     startIndex ---------------- endIndex
 *
 *   - nums[endIndex] >= nums[endIndex - 1]  -> the run continues.
 *   - nums[endIndex] <  nums[endIndex - 1]  -> the run breaks; the new run
 *                                              must start at endIndex.
 *
 * Invariant: at every endIndex, nums[startIndex ... endIndex] is the longest
 * non-decreasing contiguous subarray ENDING at endIndex.
 *
 * APPROACHES
 *   Brute force : from every start, extend right until order breaks. O(n^2)
 *                 time / O(1) space. Repeats work the previous start already did.
 *   Optimal     : single scan tracking where the current run started (below).
 *
 * COMPLEXITY
 *   Time O(n)   Space O(1)
 * ----------------------------------------------------------------------------
 */

public class LongestNonDecreasingSubarray {

    public int longestNonDecreasingSubarray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int startIndex = 0;
        int maxLength = 1;

        for (int endIndex = 1; endIndex < nums.length; endIndex++) {

            // The current pair breaks non-decreasing order. Since the result
            // must be contiguous, start a new run here.
            if (nums[endIndex] < nums[endIndex - 1]) {
                startIndex = endIndex;
            }

            // nums[startIndex ... endIndex] is currently non-decreasing.
            int currentLength = endIndex - startIndex + 1;
            maxLength = Math.max(maxLength, currentLength);
        }

        return maxLength;
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP: change AT MOST ONE element to any integer, then return the
     * longest non-decreasing contiguous subarray length.
     *
     * MENTAL MAP
     *   Precompute two arrays:
     *     left[i]  = length of the longest non-decreasing run ENDING at i.
     *     right[i] = length of the longest non-decreasing run STARTING at i.
     *
     *   For each index i we consider changing nums[i]:
     *     - Extend leftward:  set nums[i] >= nums[i-1]  -> left[i-1] + 1
     *     - Extend rightward: set nums[i] <= nums[i+1]  -> 1 + right[i+1]
     *     - BRIDGE both sides: only if nums[i-1] <= nums[i+1] can one value sit
     *       between them -> left[i-1] + 1 + right[i+1].
     *   Answer = max over all i (and handle array ends where i-1 or i+1 is out
     *   of bounds). The no-change baseline is just max(left[i]).
     *
     * COMPLEXITY  Time O(n)   Space O(n)
     * ------------------------------------------------------------------------
     */
    public int longestAfterOneChange(int[] nums) {
        // TODO:
        //  1. handle null / short arrays (length <= 2 -> whole array works).
        //  2. build left[]  : left[i]  = (nums[i] >= nums[i-1]) ? left[i-1]+1 : 1
        //  3. build right[] : right[i] = (nums[i] <= nums[i+1]) ? right[i+1]+1 : 1
        //  4. for each i: best = max(left[i-1]+1, 1+right[i+1], and bridge
        //     left[i-1]+1+right[i+1] when nums[i-1] <= nums[i+1]); guard ends.
        //  5. return the running max.
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        if (n <= 2) {
            return n;
        }
        int[] end = new int[n]; //end[i], max subarray length ending at i
        int[] start = new int[n]; //max subarray length starting at i

        end[0] = 1;
        for(int i = 1; i < n; i++)
        {
            if(nums[i] >= nums[i - 1])
            {
                end[i] = end[i - 1] + 1;
            }
            else
            {
                end[i] = 1;
            }
        }

        start[n - 1] = 1;
        for(int i = n - 2; i >= 0; i--)
        {
            if(nums[i] <= nums[i + 1])
            {
                start[i] = start[i + 1] + 1;
            }
            else
            {
                start[i] = 1;
            }
        }

    int ans = 1;
    for (int i = 0; i < n; i++) {
        int left = (i - 1 >= 0) ? end[i - 1] : 0;   // run just left of i
        int right = (i + 1 < n) ? start[i + 1] : 0; // run just right of i

        int best;
        // Bridge both sides only if a single value can sit between them.
        if (i - 1 >= 0 && i + 1 < n && nums[i - 1] <= nums[i + 1]) {
            best = left + 1 + right;
        } else {
            // Otherwise attach the changed element to whichever side is longer.
            best = Math.max(left + 1, right + 1);
        }
        ans = Math.max(ans, best);
    }

    return ans;
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        LongestNonDecreasingSubarray sol = new LongestNonDecreasingSubarray();

        System.out.println(sol.longestNonDecreasingSubarray(new int[] {1, 2, 2, 1, 3, 4})); // 3
        System.out.println(sol.longestNonDecreasingSubarray(new int[] {5, 4, 3, 2, 1}));    // 1
        System.out.println(sol.longestNonDecreasingSubarray(new int[] {1, 2, 3, 4, 5}));    // 5
        System.out.println(sol.longestNonDecreasingSubarray(new int[] {7}));                // 1
        System.out.println(sol.longestNonDecreasingSubarray(new int[] {}));                 // 0

        // Follow-up: change at most one element.
        System.out.println(sol.longestAfterOneChange(new int[] {1, 5, 2, 3}));       // 4
        System.out.println(sol.longestAfterOneChange(new int[] {5, 1, 2, 3}));       // 4
        System.out.println(sol.longestAfterOneChange(new int[] {1, 2, 3, 1}));       // 4
        System.out.println(sol.longestAfterOneChange(new int[] {1, 2, 2, 1, 3, 4})); // 6
        System.out.println(sol.longestAfterOneChange(new int[] {5, 4, 3, 2, 1}));    // 2
        System.out.println(sol.longestAfterOneChange(new int[] {7}));                // 1
    }
}
