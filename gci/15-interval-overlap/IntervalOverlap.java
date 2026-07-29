/*
 * ============================================================================
 * Problem 15 (Google L4 prep) — Do Two Intervals Overlap?
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given two CLOSED intervals first = [start1, end1] and second = [start2, end2],
 * decide whether they share at least one point. Endpoints count, so [1,4] and
 * [4,7] overlap at 4.
 *
 * This is the base of a reported Google progression. Follow-ups (NOT solved
 * here): whether ANY pair in a list overlaps, max intervals active at once,
 * insert an interval, merge overlaps. We solve only the base problem now.
 *
 * EXAMPLE
 *   first = [2, 6], second = [5, 8]  ->  true   (they share [5, 6])
 *   first = [2, 4], second = [5, 8]  ->  false  (one finishes before the other)
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: interval intersection by boundary comparison.
 *
 * Two intervals overlap UNLESS one finishes strictly before the other starts.
 * Flip that "unless" and you get the positive test directly:
 *
 *     firstStart <= secondEnd   AND   secondStart <= firstEnd
 *
 *   - firstStart <= secondEnd : first begins by the time second ends.
 *   - secondStart <= firstEnd : second begins by the time first ends.
 *   Both must hold for the ranges to touch.
 *
 * Equivalent non-overlap condition (the mirror image):
 *     firstEnd < secondStart  ||  secondEnd < firstStart
 *
 * Invariant: each interval is valid, i.e. start <= end.
 *
 * APPROACHES
 *   Brute force : scan every integer point shared by both. Only works for small
 *                 integer coords, costs O(coordinate range). Wasteful.
 *   Optimal     : two boundary comparisons (below).
 *
 * COMPLEXITY
 *   Time O(1)   Space O(1)
 * ----------------------------------------------------------------------------
 */

public class IntervalOverlap {

    public boolean overlaps(int[] first, int[] second) {
        // Each interval must contain exactly two valid boundaries.
        if (first == null || second == null ||
            first.length != 2 || second.length != 2 ||
            first[0] > first[1] || second[0] > second[1]) {
            throw new IllegalArgumentException("Invalid interval");
        }

        int firstStart = first[0];
        int firstEnd = first[1];
        int secondStart = second[0];
        int secondEnd = second[1];

        // Closed intervals overlap when each one starts before (or exactly when)
        // the other one ends.
        return firstStart <= secondEnd && secondStart <= firstEnd;
    }

    // ------------------------------------------------------------------------
    // Self-test: run with `java -ea IntervalOverlap` so the assertions fire.
    // ------------------------------------------------------------------------
    public static void main(String[] args) {
        IntervalOverlap solver = new IntervalOverlap();

        // Partial overlap.
        assert solver.overlaps(new int[]{2, 6}, new int[]{5, 8});
        // Disjoint: second starts after first ends.
        assert !solver.overlaps(new int[]{2, 4}, new int[]{5, 8});
        // Touch at a single shared endpoint (closed intervals).
        assert solver.overlaps(new int[]{1, 4}, new int[]{4, 7});
        // One fully contains the other.
        assert solver.overlaps(new int[]{0, 10}, new int[]{3, 4});
        // Identical intervals.
        assert solver.overlaps(new int[]{5, 5}, new int[]{5, 5});
        // Disjoint the other way: first starts after second ends.
        assert !solver.overlaps(new int[]{9, 12}, new int[]{1, 3});

        // Invalid inputs must be rejected.
        boolean threw = false;
        try {
            solver.overlaps(new int[]{4, 2}, new int[]{1, 3});
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        assert threw;

        System.out.println("All IntervalOverlap tests passed.");
    }
}
