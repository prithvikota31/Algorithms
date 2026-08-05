import java.util.ArrayList;
import java.util.List;

/*
 * ============================================================================
 * Problem 26 (Google L4 prep) — Array Jump: Take-or-Skip Max Score
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * At index i you have two choices:
 *   - SKIP arr[i]  -> move to i + 1, gain 0.
 *   - TAKE arr[i]  -> gain arr[i], then jump to i + arr[i].
 * All values are positive, so every move goes strictly forward. Once the
 * index is outside the array, the game ends. Maximize total score.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   Does arr[i] represent BOTH the score gained and the jump length? This
 *   solution assumes yes (a single array driving both).
 *
 * EXAMPLE
 *   arr = [2, 1, 3, 4]
 *   Take index 0 (score 2) -> jump to index 2.
 *   Skip index 2           -> move to index 3.
 *   Take index 3 (score 4) -> jump outside the array.
 *   Total score = 2 + 4 = 6.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: DYNAMIC PROGRAMMING, take-or-skip.
 *
 *   dp[i] = maximum score achievable starting from index i.
 *
 * Once we are at index i, earlier decisions no longer matter — we only need
 * the best result available from whichever index comes next:
 *   - dp[i + 1]            if we skip
 *   - arr[i] + dp[i+arr[i]] if we take (0 if i+arr[i] lands outside the array)
 *
 * Invariant: after computing dp[i], it holds the optimal score for every
 * valid sequence of choices beginning at index i.
 *
 * Both destinations (i+1 and i+arr[i]) are strictly greater than i, so
 * computing dp from right to left guarantees every value dp[i] depends on is
 * already known.
 *
 * APPROACHES
 *   Brute force : recursively try both choices at every index.
 *                 solve(i) = max(solve(i+1), arr[i] + solve(i+arr[i]))
 *                 Repeats the same states many times. Time O(2^n), Space O(n)
 *                 (recursion depth).
 *   Optimal     : bottom-up DP scanning right to left (below).
 *                 Time O(n)   Space O(n)
 * ----------------------------------------------------------------------------
 */
public class ArrayJumpMaxScore {

    public long maxScore(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }

        int n = arr.length;

        // dp[i] = max score starting at index i. dp[n] = outside the array = 0.
        long[] dp = new long[n + 1];

        for (int i = n - 1; i >= 0; i--) {
            long skipScore = dp[i + 1];

            int jumpIndex = i + arr[i];
            long scoreAfterJump = jumpIndex < n ? dp[jumpIndex] : 0;
            long takeScore = arr[i] + scoreAfterJump;

            dp[i] = Math.max(skipScore, takeScore);
        }

        return dp[0];
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): return the selected indices, not just the
     * score. Standard DP-reconstruction pattern — reusable for LIS, knapsack,
     * shortest paths, and scheduling problems.
     *
     * MENTAL MAP
     *   Record, alongside dp[i], WHICH choice produced it (took[i] = true
     *   means "take" was at least as good as "skip"). Then walk forward from
     *   index 0, following took[i] to rebuild the exact sequence of taken
     *   indices.
     * ------------------------------------------------------------------------
     */
    public List<Integer> maxScoreIndices(int[] arr) {
        List<Integer> takenIndices = new ArrayList<>();
        if (arr == null || arr.length == 0) {
            return takenIndices;
        }

        int n = arr.length;
        long[] dp = new long[n + 1];
        boolean[] took = new boolean[n];

        for (int i = n - 1; i >= 0; i--) {
            long skipScore = dp[i + 1];

            int jumpIndex = i + arr[i];
            long scoreAfterJump = jumpIndex < n ? dp[jumpIndex] : 0;
            long takeScore = arr[i] + scoreAfterJump;

            if (takeScore >= skipScore) {
                dp[i] = takeScore;
                took[i] = true;
            } else {
                dp[i] = skipScore;
                took[i] = false;
            }
        }

        int i = 0;
        while (i < n) {
            if (took[i]) {
                takenIndices.add(i);
                i += arr[i];
            } else {
                i += 1;
            }
        }

        return takenIndices;
    }

    public static void main(String[] args) {
        ArrayJumpMaxScore solution = new ArrayJumpMaxScore();

        check("worked example", solution.maxScore(new int[] { 2, 1, 3, 4 }), 6L);
        check("single element", solution.maxScore(new int[] { 5 }), 5L);
        check("take chains into a bigger value", solution.maxScore(new int[] { 1, 100 }), 101L);
        check("empty array", solution.maxScore(new int[] {}), 0L);

        check("worked example indices", solution.maxScoreIndices(new int[] { 2, 1, 3, 4 }), List.of(0, 3));
        check("single element indices", solution.maxScoreIndices(new int[] { 5 }), List.of(0));

        System.out.println("all passed");
    }

    private static void check(String name, long actual, long expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }

    private static void check(String name, List<Integer> actual, List<Integer> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
