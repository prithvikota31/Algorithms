/*
 * ============================================================================
 * Problem 15 — Follow-up B: Maximum Intervals Overlapping Simultaneously
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given an array of CLOSED intervals, return the greatest number of intervals
 * that cover a single point at the same time.
 *
 * EXAMPLE
 *   [[1,4],[2,5],[4,7]] -> 3   (all three cover the point 4)
 *   [[1,2],[3,4],[5,6]] -> 1   (never two at once)
 *   [[1,4],[4,7]]       -> 2   (closed intervals share the point 4)
 *
 * ----------------------------------------------------------------------------
 * Fill in maxOverlap(). The main() self-test below is ready to run:
 *     javac MaxSimultaneousOverlap.java && java -ea MaxSimultaneousOverlap
 * ----------------------------------------------------------------------------
 */

import java.util.Arrays;
import java.util.PriorityQueue;

public class MaxSimultaneousOverlap {

    public int maxOverlap(int[][] intervals) {
        int n = intervals.length;
        if(n <= 1)  return n;

        //lets user heap
        Arrays.sort(intervals, (a, b) -> (a[0] - b[0]));
        PriorityQueue<Integer> endTimesMinHeap = new PriorityQueue<>();

        for(int[] interval: intervals)
        {
            if(!endTimesMinHeap.isEmpty() && endTimesMinHeap.peek() < interval[0])
            {
                //one of previous meeting ended, so we can occupty that place
                endTimesMinHeap.poll();
            }
            endTimesMinHeap.offer(interval[1]);
        }
        return endTimesMinHeap.size();
    }

    // ------------------------------------------------------------------------
    // Self-test harness. Run with `java -ea MaxSimultaneousOverlap`.
    // ------------------------------------------------------------------------
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, int expected, int[][] intervals) {
        int actual = new MaxSimultaneousOverlap().maxOverlap(intervals);
        if (actual == expected) {
            passed++;
            System.out.println("pass " + name);
        } else {
            failed++;
            System.out.println("FAIL " + name + " -> expected " + expected + " got " + actual);
        }
    }

    public static void main(String[] args) {
        check("triple-at-4",   3, new int[][]{{1, 4}, {2, 5}, {4, 7}});
        check("all-disjoint",  1, new int[][]{{1, 2}, {3, 4}, {5, 6}});
        check("touch-closed",  2, new int[][]{{1, 4}, {4, 7}});
        check("wide-plus-one", 2, new int[][]{{0, 10}, {1, 2}, {3, 4}});
        check("nested",        3, new int[][]{{1, 10}, {2, 9}, {3, 8}});
        check("all-identical", 3, new int[][]{{2, 5}, {2, 5}, {2, 5}});
        check("point-interval",1, new int[][]{{5, 5}});
        check("empty",         0, new int[][]{});

        System.out.println("----");
        System.out.println("passed=" + passed + " failed=" + failed);
        assert failed == 0 : failed + " test(s) failed";
    }
}
