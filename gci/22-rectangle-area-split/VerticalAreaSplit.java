import java.util.Arrays;
import java.util.Random;

/*
 * ============================================================================
 * Problem 22 (Google L4 prep) - Vertical line splitting rectangle area equally
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given axis-aligned rectangles [x1, y1, x2, y2], find a vertical line x = k
 * such that the rectangle area left of the line equals the area to its right.
 * Rectangles may overlap; overlapping area is counted once per rectangle
 * (areas simply add -- this is not a union-area problem). The two supplied
 * corners may appear in either order.
 *
 * EXAMPLES
 * --------
 * [[0,0,2,2], [2,0,4,2]] -> 2.0  (4 + 4, cut on the shared boundary)
 * [[0,0,2,2], [1,0,3,2]] -> 1.5  (overlapped strip counted twice)
 * [[0,0,2,2], [5,0,7,2]] -> 2.0  (the gap contributes nothing)
 *
 * INTUITION
 * ---------
 * Sweep a vertical line left to right. A rectangle influences how fast area
 * accumulates only while the line sits inside its x-range, and it does so at
 * a constant rate equal to its height. So each rectangle becomes two events:
 * +height at x1 and -height at x2. Between consecutive event coordinates the
 * active height is constant, so area grows linearly there and the exact cut
 * inside a strip can be solved for directly instead of searched for.
 *
 * Sorting rectangles by x1 and consuming them one at a time is NOT enough:
 * rectangles sharing an x-range must contribute their heights simultaneously,
 * which only the event view captures.
 *
 * ALGORITHM
 * ---------
 * 1. Normalize each pair of corners, emit events (startX, +height) and
 *    (endX, -height), and accumulate totalArea using long differences.
 * 2. Sort events by x; targetArea = totalArea / 2.
 * 3. Walk the events. For the strip [previousX, currentX):
 *      stripArea = (currentX - previousX) * activeHeight
 *      if currentArea + stripArea >= targetArea, the cut is inside the strip:
 *          answer = previousX + (targetArea - currentArea) / activeHeight
 *      otherwise consume the strip and apply every event at currentX.
 * 4. All events sharing an x are applied before the next strip is measured.
 *
 * COMPLEXITY
 * ----------
 * Time:  O(N log N) to sort 2N events, then O(N) to sweep.
 * Space: O(N) for the events.
 * ============================================================================
 */
public class VerticalAreaSplit {

    public double findVerticalCut(int[][] rectangles) {
        if(rectangles == null || rectangles.length == 0)
        {
            return 0;
        }

        //rectangle (x1, y1, x2, y2) diagonal coordinates
        int n = rectangles.length;
        long[][] events = new long[2 * n][2];
        //each event is (x, height it adds/subtracts)
        double totalArea = 0;
        int ind = 0;
        for(int[] rectangle: rectangles)
        {
            int x1 = rectangle[0];
            int y1 = rectangle[1];
            int x2 = rectangle[2];
            int y2 = rectangle[3];

            long startX = Math.min((long) x1, x2);
            long endX = Math.max((long) x1, x2);
            long height = Math.abs((long) y2 - y1);

            totalArea += (double) (endX - startX) * height;
            events[ind][1] = height;
            events[ind++][0] = startX;
            events[ind][1] = -height;
            events[ind++][0] = endX;
        }

        // so now we have all events
        //lets sweep across the events one strip at a time
        Arrays.sort(events, (a, b) -> Long.compare(a[0], b[0]));

        double targetArea = totalArea / 2.0;
        long previousStripX = events[0][0];
        long activeHeight = 0;
        double areaSoFar = 0;
        int i = 0;

        while(i < events.length)
        {
            long stripEndX = events[i][0];
            long stripStartX = previousStripX;
            long stripWidth = stripEndX - stripStartX;
            double stripArea = (double) stripWidth * activeHeight;

            if(activeHeight > 0 && areaSoFar + stripArea >= targetArea)
            {
                double remainingArea = targetArea - areaSoFar;
                double extend = remainingArea / activeHeight;

                return stripStartX + extend;

            }
            areaSoFar += stripArea;

            while(i < events.length && events[i][0] == stripEndX)
            {
                activeHeight += events[i][1];
                i++;
            }

            previousStripX = stripEndX;
        }

        return previousStripX;


    }

    public static void main(String[] args) {
        VerticalAreaSplit sol = new VerticalAreaSplit();

        // Single rectangle [0,0,4,2] -> area 8, half at x=2.
        check("single rectangle", sol.findVerticalCut(new int[][] {
                {0, 0, 4, 2}
        }), 2.0);

        // Two contiguous rectangles, cut lands exactly at the boundary.
        check("cut at boundary between rectangles", sol.findVerticalCut(new int[][] {
                {0, 0, 4, 2}, {4, 0, 6, 4}
        }), 4.0);

        // Three equal-height contiguous rectangles, cut lands mid-rectangle.
        check("cut lands mid-rectangle", sol.findVerticalCut(new int[][] {
                {0, 0, 2, 3}, {2, 0, 4, 3}, {4, 0, 6, 3}
        }), 3.0);

        // Unsorted input -- sort step must still find the correct cut.
        check("unsorted input", sol.findVerticalCut(new int[][] {
                {4, 0, 6, 4}, {0, 0, 4, 2}
        }), 4.0);

        // Rectangles with a gap between them; gap contributes zero area.
        check("gap between rectangles", sol.findVerticalCut(new int[][] {
                {0, 0, 2, 2}, {5, 0, 7, 2}
        }), 2.0);

        // Identical rectangles: the shared area is counted twice.
        check("full overlap", sol.findVerticalCut(new int[][] {
                {0, 0, 4, 2}, {0, 0, 4, 2}
        }), 2.0);

        // Partial x-overlap; a plain left-to-right rectangle scan answers 2.0.
        check("partial overlap", sol.findVerticalCut(new int[][] {
                {0, 0, 2, 2}, {1, 0, 3, 2}
        }), 1.5);

        // Stacked rectangles share an x-range and contribute heights together.
        check("stacked same x-range", sol.findVerticalCut(new int[][] {
                {0, 0, 2, 1}, {0, 1, 2, 3}
        }), 1.0);

        // A tall rectangle nested inside a wide one pulls the cut left.
        check("nested x-range", sol.findVerticalCut(new int[][] {
                {0, 0, 8, 1}, {3, 0, 4, 4}
        }), 3.6);

        check("negative coordinates", sol.findVerticalCut(new int[][] {
                {-4, -2, 0, 2}, {0, -2, 4, 2}
        }), 0.0);

        // Combined active height exceeds int but fits in long.
        check("large overlapping heights", sol.findVerticalCut(new int[][] {
            {0, -1_000_000_000, 1, 1_000_000_000},
            {0, -1_000_000_000, 1, 1_000_000_000}
        }), 0.5);

        // Coordinate subtraction must widen before spanning this range.
        check("full int x-range", sol.findVerticalCut(new int[][] {
            {Integer.MIN_VALUE, 0, Integer.MAX_VALUE, 1}
        }), -0.5);

        check("reversed diagonal corners", sol.findVerticalCut(new int[][] {
            {4, 3, 0, 1}
        }), 2.0);

        verifyAreaBalance(sol);
        System.out.println("all passed");
    }

    /** Checks random inputs by measuring the area left of the reported cut. */
    private static void verifyAreaBalance(VerticalAreaSplit sol) {
        Random random = new Random(22);

        for (int trial = 0; trial < 2000; trial++) {
            int count = 1 + random.nextInt(6);
            int[][] rectangles = new int[count][];

            for (int i = 0; i < count; i++) {
                int startX = random.nextInt(21) - 10;
                int startY = random.nextInt(21) - 10;
                rectangles[i] = new int[] {
                    startX, startY,
                    startX + 1 + random.nextInt(8),
                    startY + 1 + random.nextInt(8)
                };
            }

            double cut = sol.findVerticalCut(rectangles);
            double leftArea = areaLeftOf(rectangles, cut);
            double totalArea = areaLeftOf(rectangles, Double.POSITIVE_INFINITY);

            if (Math.abs(leftArea - totalArea / 2.0) > 1e-6) {
                throw new AssertionError("FAIL random trial: cut " + cut
                        + " gives left " + leftArea + " want " + totalArea / 2.0
                        + " for " + Arrays.deepToString(rectangles));
            }
        }
        System.out.println("pass randomized area-balance check");
    }

    private static double areaLeftOf(int[][] rectangles, double x) {
        double area = 0;
        for (int[] rect : rectangles) {
            double width = Math.min(x, rect[2]) - rect[0];
            if (width > 0) {
                area += width * (rect[3] - rect[1]);
            }
        }
        return area;
    }

    private static void check(String name, double actual, double expected) {
        if (Math.abs(actual - expected) > 1e-9) {
            throw new AssertionError("FAIL " + name + ": got " + actual
                    + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
