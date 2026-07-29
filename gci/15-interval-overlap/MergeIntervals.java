/*
 * ============================================================================
 * Problem 15 — Follow-up D: Merge All Overlapping Intervals (LC 56)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given CLOSED intervals in any order, merge every overlapping group and
 * return the combined, sorted, non-overlapping list.
 *
 * EXAMPLE
 *   [[1,3],[2,6],[8,10],[9,12]] -> [[1,6],[8,12]]
 *   [[1,4],[4,5]]               -> [[1,5]]   (touch at 4, closed)
 *   [[1,2],[3,4]]               -> [[1,2],[3,4]]
 *
 * ----------------------------------------------------------------------------
 * Fill in merge(). The main() self-test below is ready to run:
 *     javac MergeIntervals.java && java -ea MergeIntervals
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {

    public int[][] merge(int[][] intervals) {
        int n = intervals.length;
        if(n <= 1)  return intervals;

        //lets sort by start
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> result = new ArrayList<>();
        result.add(intervals[0]);

        for(int i = 1; i < n; i++)
        {
            int[] lastInterval = result.get(result.size() - 1);
            int[] current = intervals[i];

            if(current[0] <= lastInterval[1] && lastInterval[0] <= current[1])// overlaps
            {
                lastInterval[0] = Math.min(lastInterval[0], current[0]);
                lastInterval[1] = Math.max(lastInterval[1], current[1]);
            }
            else //no overlap
            {
                result.add(current);
            }

        }


        return result.toArray(new int[0][]);


    }

    // ------------------------------------------------------------------------
    // Self-test harness. Run with `java -ea MergeIntervals`.
    // ------------------------------------------------------------------------
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, int[][] expected, int[][] intervals) {
        int[][] actual;
        try {
            actual = new MergeIntervals().merge(intervals);
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL " + name + " -> threw " + e);
            return;
        }
        if (Arrays.deepEquals(actual, expected)) {
            passed++;
            System.out.println("pass " + name);
        } else {
            failed++;
            System.out.println("FAIL " + name +
                " -> expected " + Arrays.deepToString(expected) +
                " got " + Arrays.deepToString(actual));
        }
    }

    public static void main(String[] args) {
        // Two separate merged groups.
        check("two-groups",  new int[][]{{1, 6}, {8, 12}},
                             new int[][]{{1, 3}, {2, 6}, {8, 10}, {9, 12}});
        // Touching endpoints merge (closed intervals).
        check("touch-merge", new int[][]{{1, 5}},
                             new int[][]{{1, 4}, {4, 5}});
        // Fully disjoint stays as-is.
        check("disjoint",    new int[][]{{1, 2}, {3, 4}},
                             new int[][]{{1, 2}, {3, 4}});
        // Unsorted input must be handled.
        check("unsorted",    new int[][]{{1, 2}, {3, 5}},
                             new int[][]{{3, 5}, {1, 2}});
        // One interval swallows another.
        check("nested",      new int[][]{{1, 10}},
                             new int[][]{{1, 10}, {2, 3}});
        // Chain that all collapses into one.
        check("chain",       new int[][]{{1, 6}},
                             new int[][]{{1, 4}, {2, 5}, {3, 6}});
        // Edge sizes.
        check("single",      new int[][]{{5, 7}}, new int[][]{{5, 7}});
        check("empty",       new int[][]{},       new int[][]{});

        System.out.println("----");
        System.out.println("passed=" + passed + " failed=" + failed);
        assert failed == 0 : failed + " test(s) failed";
    }
}
