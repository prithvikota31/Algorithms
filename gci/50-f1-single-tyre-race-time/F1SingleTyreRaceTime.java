/*
 * ============================================================================
 * Problem 50 (Google L4 prep) - F1 Race Using One Tyre
 * ============================================================================
 *
 * PROMPT
 * ------
 * Each tyre is [f, r]: its first lap takes f time and each later lap takes r
 * times the previous lap. Complete numLaps without changing tyres and return
 * the minimum total time among all available tyre types.
 *
 * EXAMPLES
 * --------
 * tires = [[2, 3], [3, 2]], numLaps = 3 -> 21
 * tires = [[5, 1], [2, 3]], numLaps = 4 -> 20
 * tires = [[7, 4]], numLaps = 1         -> 7
 *
 * INTUITION
 * ---------
 * Once a tyre is selected, there are no more decisions: its lap times are the
 * geometric sequence f, f*r, f*r^2, ... . Simulate that sequence for every
 * tyre and keep the smallest complete-race total.
 *
 * ALGORITHM
 * ---------
 * 1. For each tyre, start currentLapTime at f and totalTime at zero.
 * 2. Add each lap time to the total and multiply it by r for the next lap.
 * 3. Keep the smallest complete-race total across all tyres.
 * Assume all calculated race times fit in a long.
 *
 * COMPLEXITY
 * ----------
 * Time: O(T * L), where T is the tyre count and L is numLaps.
 * Space: O(1).
 * ============================================================================
 */
public class F1SingleTyreRaceTime {

    public long minimumRaceTime(int[][] tires, int numLaps) {
        if (numLaps <= 0) {
            return 0;
        }
        if (tires == null || tires.length == 0) {
            throw new IllegalArgumentException("at least one tyre is required");
        }

        long bestRaceTime = Long.MAX_VALUE;

        for (int[] tire : tires) {
            if (tire == null || tire.length < 2 || tire[0] <= 0 || tire[1] <= 0) {
                throw new IllegalArgumentException("each tyre must contain positive f and r");
            }

            long currentLapTime = tire[0];
            long totalTime = 0;

            for (int lap = 0; lap < numLaps; lap++) {
                totalTime += currentLapTime;
                currentLapTime *= tire[1];
            }

            bestRaceTime = Math.min(bestRaceTime, totalTime);
        }

        return bestRaceTime;
    }

    public static void main(String[] args) {
        F1SingleTyreRaceTime solution = new F1SingleTyreRaceTime();

        check("worked example", solution.minimumRaceTime(
                new int[][] { { 2, 3 }, { 3, 2 } }, 3), 21L);
        check("constant tyre wins", solution.minimumRaceTime(
                new int[][] { { 5, 1 }, { 2, 3 } }, 4), 20L);
        check("single lap", solution.minimumRaceTime(
                new int[][] { { 7, 4 }, { 9, 2 } }, 1), 7L);
        check("zero laps", solution.minimumRaceTime(
                new int[][] { { 2, 3 } }, 0), 0L);
        check("requires long result", solution.minimumRaceTime(
                new int[][] { { Integer.MAX_VALUE, 1 } }, 3), 3L * Integer.MAX_VALUE);

        System.out.println("all passed");
    }

    private static void check(String name, long actual, long expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
