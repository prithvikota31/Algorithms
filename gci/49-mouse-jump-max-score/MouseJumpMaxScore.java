/*
 * ============================================================================
 * Problem 49 (Google L4 prep) - Mouse Jump Maximum Score
 * ============================================================================
 *
 * PROMPT
 * ------
 * Start at index 0 and reach index n - 1. From index i, jump to any later
 * index j and earn (j - i) * nums[j]. Return the maximum total score.
 *
 * EXAMPLES
 * --------
 * nums = [2, 5, 1, 4]  -> 13, using 0 -> 1 -> 3
 * nums = [2, 1, 1, 10] -> 30, using 0 -> 3
 * nums = [7]            -> 0, because no jump is needed
 *
 * INTUITION
 * ---------
 * A jump i -> j crosses (j - i) unit gaps, and every crossed gap earns
 * nums[j]. For each gap, the best possible landing value is therefore the
 * maximum value at any index to its right.
 *
 * These choices are simultaneously achievable: scanning from right to left,
 * whenever a new suffix maximum appears, make that index the next landing
 * point. Those suffix-maximum indices form an increasing path from 0 to n - 1.
 * Thus the answer is the sum of the suffix maximum to the right of every gap.
 *
 * ALGORITHM
 * ---------
 * 1. Scan the unit gaps from right to left.
 * 2. Maintain the maximum nums value strictly to the right of the gap.
 * 3. Add that maximum to the answer for each gap.
 *
 * COMPLEXITY
 * ----------
 * Time: O(n), where n is nums.length.
 * Space: O(1).
 * ============================================================================
 */
public class MouseJumpMaxScore {

    public long maxScore(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }

        long score = 0;
        int maxRight = nums[nums.length - 1];

        for (int i = nums.length - 2; i >= 0; i--) {
            maxRight = Math.max(maxRight, nums[i + 1]);
            score += maxRight;
        }

        return score;
    }

    public static void main(String[] args) {
        MouseJumpMaxScore solution = new MouseJumpMaxScore();

        check("worked example", solution.maxScore(new int[] { 2, 5, 1, 4 }), 13L);
        check("best to jump directly", solution.maxScore(new int[] { 2, 1, 1, 10 }), 30L);
        check("decreasing values", solution.maxScore(new int[] { 9, 7, 4, 1 }), 12L);
        check("negative values", solution.maxScore(new int[] { 0, -5, -1, -4 }), -6L);
        check("single element", solution.maxScore(new int[] { 7 }), 0L);
        check("null array", solution.maxScore(null), 0L);
        check("long result", solution.maxScore(new int[] { 0, Integer.MAX_VALUE, Integer.MAX_VALUE }),
                2L * Integer.MAX_VALUE);

        verifyAgainstQuadraticDp(solution);
        System.out.println("all passed");
    }

    private static void verifyAgainstQuadraticDp(MouseJumpMaxScore solution) {
        for (int length = 1; length <= 7; length++) {
            int[] nums = new int[length];
            verifyArrays(solution, nums, 0);
        }
        System.out.println("pass exhaustive comparison with quadratic DP");
    }

    private static void verifyArrays(MouseJumpMaxScore solution, int[] nums, int index) {
        if (index == nums.length) {
            long actual = solution.maxScore(nums);
            long expected = maxScoreQuadratic(nums);
            if (actual != expected) {
                throw new AssertionError("FAIL exhaustive check: got " + actual + " want " + expected);
            }
            return;
        }

        for (int value = -2; value <= 3; value++) {
            nums[index] = value;
            verifyArrays(solution, nums, index + 1);
        }
    }

    private static long maxScoreQuadratic(int[] nums) {
        long[] dp = new long[nums.length];

        for (int destination = 1; destination < nums.length; destination++) {
            dp[destination] = Long.MIN_VALUE;
            for (int source = 0; source < destination; source++) {
                long jumpScore = (long) (destination - source) * nums[destination];
                dp[destination] = Math.max(dp[destination], dp[source] + jumpScore);
            }
        }

        return dp[nums.length - 1];
    }

    private static void check(String name, long actual, long expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}