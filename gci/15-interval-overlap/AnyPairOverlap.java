/*
 * ============================================================================
 * Problem 15 — Follow-up A: Does ANY pair of intervals overlap?
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given an array of CLOSED intervals, return true if ANY pair overlaps
 * (touching at an endpoint counts), otherwise false.
 *
 * EXAMPLE
 *   [[1,3],[5,7],[2,4]]  -> true   ([1,3] and [2,4] overlap)
 *   [[1,2],[3,4],[5,6]]  -> false
 *   [[1,4],[4,7]]        -> true   (touch at 4)
 *
 * ----------------------------------------------------------------------------
 * Fill in anyOverlap(). The main() self-test below is ready to run:
 *     javac AnyPairOverlap.java && java -ea AnyPairOverlap
 * ----------------------------------------------------------------------------
 */

import java.util.Arrays;

public class AnyPairOverlap {

    public boolean anyOverlap(int[][] intervals) {
        int n = intervals.length;
        if(n <= 1)  return false;
        //sort by end value
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
        int[] lastInterval = intervals[0];
        for(int i = 1; i < n; i++)
        {
            if(lastInterval[0] <= intervals[i][1] && intervals[i][0] <= lastInterval[1])
            {
                //overlap exists
                return true;
            }
            else{
                lastInterval = intervals[i];
            }
        }

        return false;
    }

    // ------------------------------------------------------------------------
    // Self-test harness. Run with `java -ea AnyPairOverlap`.
    // ------------------------------------------------------------------------
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean expected, int[][] intervals) {
        boolean actual;
        try {
            actual = new AnyPairOverlap().anyOverlap(intervals);
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL " + name + " -> threw " + e);
            return;
        }
        if (actual == expected) {
            passed++;
            System.out.println("pass " + name);
        } else {
            failed++;
            System.out.println("FAIL " + name + " -> expected " + expected + " got " + actual);
        }
    }

    public static void main(String[] args) {
        // Basic overlap somewhere in the middle.
        check("unsorted-overlap",      true,  new int[][]{{1, 3}, {5, 7}, {2, 4}});
        // Fully disjoint.
        check("all-disjoint",          false, new int[][]{{1, 2}, {3, 4}, {5, 6}});
        // Touching endpoints count as overlap (closed intervals).
        check("touch-endpoint",        true,  new int[][]{{1, 4}, {4, 7}});
        // One interval contains another.
        check("containment",           true,  new int[][]{{0, 10}, {3, 4}});
        // Overlap only between the last two after sorting by start.
        check("overlap-at-tail",       true,  new int[][]{{1, 2}, {4, 6}, {5, 9}});
        // Adjacent but NOT touching (gap of 1).
        check("adjacent-gap",          false, new int[][]{{1, 3}, {4, 6}});
        // Duplicate intervals overlap themselves.
        check("duplicates",            true,  new int[][]{{2, 5}, {2, 5}});
        // Edge sizes: empty and single interval cannot have a pair.
        check("empty",                 false, new int[][]{});
        check("single",                false, new int[][]{{1, 100}});

        System.out.println("----");
        System.out.println("passed=" + passed + " failed=" + failed);
        // Make the assertion runner (java -ea) fail loudly if anything broke.
        assert failed == 0 : failed + " test(s) failed";
    }
}
