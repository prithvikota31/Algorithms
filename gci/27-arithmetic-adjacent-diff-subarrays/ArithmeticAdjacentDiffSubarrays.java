import java.util.Arrays;

/*
 * ============================================================================
 * Problem 27 (Google L4 prep) — Count Arithmetic Subarrays (adjacent diff ±1)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Count all CONTIGUOUS subarrays of length >= 2 where every adjacent
 * difference is consistently all +1 or all -1 (the direction may not switch
 * mid-subarray).
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   Each qualifying subarray must pick ONE direction and hold it for its
 *   whole length; [1,2,1] is NOT valid on its own (direction flips), but its
 *   length-2 pieces [1,2] and [2,1] are each counted separately.
 *
 * EXAMPLE
 *   arr = [1, 2, 3, 5, 4]
 *   Valid: [1,2], [2,3], [1,2,3], [5,4]   ->   answer = 4
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: ONE-PASS DP / running streak.
 *
 *   streak = number of valid subarrays ENDING at the current index.
 *
 * Once arr[i] - arr[i-1] holds the SAME direction as the previous step, every
 * valid subarray ending at i-1 extends by one, plus the new pair [i-1, i]
 * itself — so streak simply increments. If the direction changes (or breaks),
 * only the new pair [i-1, i] survives, so streak resets to 1 (or 0 if the
 * pair itself isn't ±1).
 *
 * Invariant: after processing index i, streak == exactly the count of valid
 * subarrays whose LAST element is arr[i]. Summing streak over all i gives the
 * total count (every valid subarray is counted exactly once, at its own
 * ending index).
 *
 * APPROACHES
 *   Brute force : check every (start, end) pair directly. O(n^2) time.
 *   Optimal     : single scan carrying a running streak (below). O(n) time.
 *
 * COMPLEXITY
 *   Time O(n)   Space O(1)
 * ----------------------------------------------------------------------------
 */
public class ArithmeticAdjacentDiffSubarrays {

    public long countArithmeticSubarrays(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }

        long totalCount = 0;

        // Number of valid subarrays ending at the current index.
        long streak = 0;

        // Direction of the current run: +1, -1, or 0 when no run exists.
        int previousDifference = 0;

        for (int i = 1; i < arr.length; i++) {
            long difference = (long) arr[i] - arr[i - 1];

            if (difference != 1 && difference != -1) {
                streak = 0;
                previousDifference = 0;
            } else if (difference == previousDifference) {
                // Extend every valid subarray ending at i - 1,
                // plus the new pair [i - 1, i].
                streak++;
            } else {
                // Direction changed (or this is the first valid pair), so
                // only the current pair is valid.
                streak = 1;
                previousDifference = (int) difference;
            }

            totalCount += streak;
        }

        return totalCount;
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): return the longest valid subarray's indices,
     * not just the total count.
     *
     * MENTAL MAP
     *   Reuses the exact same streak/previousDifference recurrence as
     *   countArithmeticSubarrays. streak already IS the run length in edges,
     *   so the run's start index is always recoverable as (i - streak) —
     *   no separate runStart bookkeeping needed.
     * ------------------------------------------------------------------------
     */
    public int[] longestArithmeticSubarray(int[] arr) {
        if (arr == null || arr.length < 2) {
            return new int[0];
        }

        int streak = 0;
        int previousDifference = 0;
        int bestStreak = 0;
        int bestEndIndex = 0;

        for (int i = 1; i < arr.length; i++) {
            long difference = (long) arr[i] - arr[i - 1];

            if (difference != 1 && difference != -1) {
                streak = 0;
                previousDifference = 0;
            } else if (difference == previousDifference) {
                streak++;
            } else {
                streak = 1;
                previousDifference = (int) difference;
            }

            if (streak > bestStreak) {
                bestStreak = streak;
                bestEndIndex = i;
            }
        }

        return bestStreak < 1 ? new int[0] : new int[] { bestEndIndex - bestStreak, bestEndIndex };
    }

    public static void main(String[] args) {
        ArithmeticAdjacentDiffSubarrays solution = new ArithmeticAdjacentDiffSubarrays();

        check("worked example", solution.countArithmeticSubarrays(new int[] { 1, 2, 3, 5, 4 }), 4L);
        check("direction reversal mid-run", solution.countArithmeticSubarrays(new int[] { 1, 2, 3, 2 }), 4L);
        check("no valid pairs", solution.countArithmeticSubarrays(new int[] { 1, 5, 10 }), 0L);
        check("single element", solution.countArithmeticSubarrays(new int[] { 7 }), 0L);
        check("empty array", solution.countArithmeticSubarrays(new int[] {}), 0L);

        check("longest indices, worked example",
                solution.longestArithmeticSubarray(new int[] { 1, 2, 3, 5, 4 }), new int[] { 0, 2 });
        check("longest indices, direction reversal",
                solution.longestArithmeticSubarray(new int[] { 1, 2, 3, 2 }), new int[] { 0, 2 });
        check("longest indices, no valid pairs",
                solution.longestArithmeticSubarray(new int[] { 1, 5, 10 }), new int[0]);

        System.out.println("all passed");
    }

    private static void check(String name, long actual, long expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }

    private static void check(String name, int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("FAIL " + name + ": got " + Arrays.toString(actual)
                    + " want " + Arrays.toString(expected));
        }
        System.out.println("pass " + name + " -> " + Arrays.toString(actual));
    }
}
