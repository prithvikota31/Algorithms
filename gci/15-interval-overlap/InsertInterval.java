/*
 * ============================================================================
 * Problem 15 — Follow-up C: Insert an Interval (LC 57)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given a list of CLOSED intervals sorted by start and guaranteed
 * non-overlapping, insert newInterval and merge where needed. Return the
 * result still sorted and non-overlapping.
 *
 * EXAMPLE
 *   intervals = [[1,3],[6,9]], newInterval = [2,5]  -> [[1,5],[6,9]]
 *   intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
 *                                                   -> [[1,2],[3,10],[12,16]]
 *   intervals = [], newInterval = [5,7]             -> [[5,7]]
 *
 * ----------------------------------------------------------------------------
 * Fill in insert(). The main() self-test below is ready to run:
 *     javac InsertInterval.java && java -ea InsertInterval
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertInterval {

    //[1, 5], [6, 8], [10, 12], [15, 17], [18, 20]
    //[9, 16]
    public int[][] insert(int[][] intervals, int[] newInterval) {
        //3 stages
        if(intervals.length == 0)   return new int[][]{newInterval};
        List<int[]> result = new ArrayList<>();

        int n = intervals.length;
        int ind = 0;
        //insert int list which finishes before newInterval starts
        while(ind < n && intervals[ind][1] < newInterval[0])
        {
            result.add(intervals[ind]);
            ind++;
        }
        //now there may be overlap, so change newInterval till all overlaps are handled
        while(ind < n && intervals[ind][0] <= newInterval[1] 
                            && newInterval[0] <= intervals[ind][1]) 
        {
            newInterval[0] = Math.min(newInterval[0], intervals[ind][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[ind][1]);
            ind++;
        }
        result.add(newInterval);

        while(ind < n)
        {
            result.add(intervals[ind]);
            ind++;
        }

        return result.toArray(new int[0][]);
    }

    // ------------------------------------------------------------------------
    // Self-test harness. Run with `java -ea InsertInterval`.
    // ------------------------------------------------------------------------
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, int[][] expected, int[][] intervals, int[] newInterval) {
        int[][] actual;
        try {
            actual = new InsertInterval().insert(intervals, newInterval);
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
        // New interval merges with one interval in the middle.
        check("merge-one",   new int[][]{{1, 5}, {6, 9}},
                             new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5});
        // New interval spans and swallows several.
        check("merge-many",  new int[][]{{1, 2}, {3, 10}, {12, 16}},
                             new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}}, new int[]{4, 8});
        // Insert into empty list.
        check("empty-list",  new int[][]{{5, 7}},
                             new int[][]{}, new int[]{5, 7});
        // New interval goes entirely before everything, no merge.
        check("before-all",  new int[][]{{1, 2}, {4, 6}, {8, 9}},
                             new int[][]{{4, 6}, {8, 9}}, new int[]{1, 2});
        // New interval goes entirely after everything, no merge.
        check("after-all",   new int[][]{{1, 2}, {4, 6}, {8, 12}},
                             new int[][]{{1, 2}, {4, 6}}, new int[]{8, 12});
        // Touching endpoints should merge (closed intervals).
        check("touch-merge", new int[][]{{1, 5}},
                             new int[][]{{1, 3}, {5, 5}}, new int[]{3, 5});
        // New interval fully inside an existing one -> unchanged.
        check("swallowed",   new int[][]{{1, 10}},
                             new int[][]{{1, 10}}, new int[]{3, 4});

        System.out.println("----");
        System.out.println("passed=" + passed + " failed=" + failed);
        assert failed == 0 : failed + " test(s) failed";
    }
}
