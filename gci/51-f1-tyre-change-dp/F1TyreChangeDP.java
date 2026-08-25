import java.util.Arrays;

/*
 * ============================================================================
 * Problem 51 (Google L4 prep) - F1 Race with Tyre Changes
 * ============================================================================
 *
 * PROMPT
 * ------
 * Each tyre [f, r] takes f time for its first lap and r times longer for each
 * consecutive lap. A tyre change costs changeTime and resets the chosen tyre.
 * Return the minimum time to complete numLaps; no change cost is paid initially.
 *
 * EXAMPLES
 * --------
 * tires = [[2,3], [3,4]], changeTime = 5, numLaps = 4 -> 21
 * tires = [[1,10], [2,2], [3,4]], changeTime = 6, numLaps = 5 -> 25
 * tires = [[5,1]], changeTime = 10, numLaps = 4 -> 20
 *
 * INTUITION
 * ---------
 * Split the race into fresh-tyre stints. First precompute bestStint[k], the
 * cheapest way to drive k consecutive laps on one tyre. Then partition the
 * race with DP: if the final stint has length k, everything before it is the
 * already-solved prefix dp[laps - k].
 *
 * ALGORITHM
 * ---------
 * 1. Simulate every tyre for every possible stint length to fill bestStint[k].
 * 2. Set dp[0] = 0 because completing zero laps costs nothing.
 * 3. For each completed-lap count, try every possible final stint length k.
 *    Add a change cost only when there is an earlier stint.
 * Assume all calculated times fit in a long.
 *
 * COMPLEXITY
 * ----------
 * Time: O(T * L + L^2), where T is the tyre count and L is numLaps.
 * Space: O(L).
 * ============================================================================
 */
public class F1TyreChangeDP {

    private static final long INF = Long.MAX_VALUE;

    public long minimumFinishTime(int[][] tires, int changeTime, int numLaps) {
        if (numLaps <= 0) {
            return 0;
        }
        validateInput(tires, changeTime);

        // best[k] = minimum time to run k consecutive laps
        // without changing the tyre.
        long[] best = new long[numLaps + 1];
        Arrays.fill(best, INF);

        // Precompute best stint cost.
        for (int[] tire : tires) {
            long lapTime = tire[0];
            long stintTime = 0;

            for (int laps = 1; laps <= numLaps; laps++) {
                stintTime += lapTime;
                best[laps] = Math.min(best[laps], stintTime);

                lapTime *= tire[1];
            }
        }

        // dp[i] = minimum total time to finish exactly i laps.
        long[] dp = new long[numLaps + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int laps = 1; laps <= numLaps; laps++) {

            // Assume the final stint contains 'stint' laps.
            for (int stint = 1; stint <= laps; stint++) {
                int previousLaps = laps - stint;

                if (previousLaps == 0) {
                    // First stint: no tyre-change cost.
                    dp[laps] = Math.min(dp[laps], best[stint]);
                } else {
                    // Finish previous laps, change tyre, then run final stint.
                    dp[laps] = Math.min(
                        dp[laps],
                        dp[previousLaps] + changeTime + best[stint]
                    );
                }
            }
        }

        return dp[numLaps];
    }

    private static void validateInput(int[][] tires, int changeTime) {
        if (tires == null || tires.length == 0) {
            throw new IllegalArgumentException("at least one tyre is required");
        }
        if (changeTime < 0) {
            throw new IllegalArgumentException("changeTime cannot be negative");
        }
        for (int[] tire : tires) {
            if (tire == null || tire.length < 2 || tire[0] <= 0 || tire[1] <= 0) {
                throw new IllegalArgumentException("each tyre must contain positive f and r");
            }
        }
    }

    public static void main(String[] args) {
        F1TyreChangeDP solution = new F1TyreChangeDP();

        check("worked example", solution.minimumFinishTime(
                new int[][] { { 2, 3 }, { 3, 4 } }, 5, 4), 21L);
        check("LeetCode example", solution.minimumFinishTime(
                new int[][] { { 1, 10 }, { 2, 2 }, { 3, 4 } }, 6, 5), 25L);
        check("constant tyre", solution.minimumFinishTime(
                new int[][] { { 5, 1 } }, 10, 4), 20L);
        check("single lap", solution.minimumFinishTime(
                new int[][] { { 4, 8 }, { 3, 9 } }, 7, 1), 3L);
        check("zero laps", solution.minimumFinishTime(
                new int[][] { { 2, 3 } }, 5, 0), 0L);

        verifyAgainstUnprunedDp(solution);
        System.out.println("all passed");
    }

    private static void verifyAgainstUnprunedDp(F1TyreChangeDP solution) {
        int[][][] tyreSets = {
                { { 2, 2 }, { 3, 3 } },
                { { 1, 5 }, { 4, 1 } },
                { { 3, 2 }, { 2, 4 }, { 5, 1 } }
        };

        for (int[][] tires : tyreSets) {
            for (int changeTime = 0; changeTime <= 8; changeTime++) {
                for (int numLaps = 1; numLaps <= 8; numLaps++) {
                    long actual = solution.minimumFinishTime(tires, changeTime, numLaps);
                    long expected = minimumFinishTimeUnpruned(tires, changeTime, numLaps);
                    if (actual != expected) {
                        throw new AssertionError("FAIL oracle comparison: got " + actual
                                + " want " + expected);
                    }
                }
            }
        }
        System.out.println("pass comparison with unpruned DP");
    }

    private static long minimumFinishTimeUnpruned(int[][] tires, int changeTime, int numLaps) {
        long[] dp = new long[numLaps + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int lapsCompleted = 1; lapsCompleted <= numLaps; lapsCompleted++) {
            for (int[] tire : tires) {
                long currentLapTime = tire[0];
                long stintTime = 0;

                for (int stintLength = 1; stintLength <= lapsCompleted; stintLength++) {
                    stintTime += currentLapTime;
                    int earlierLaps = lapsCompleted - stintLength;
                    long candidateTime = stintTime;

                    if (earlierLaps > 0) {
                        candidateTime += dp[earlierLaps] + changeTime;
                    }

                    dp[lapsCompleted] = Math.min(
                            dp[lapsCompleted],
                            candidateTime);
                    currentLapTime *= tire[1];
                }
            }
        }

        return dp[numLaps];
    }

    private static void check(String name, long actual, long expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
