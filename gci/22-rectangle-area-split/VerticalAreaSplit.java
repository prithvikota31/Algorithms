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
 * (areas simply add -- this is not a union-area problem).
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
 * 1. Emit events (x1, +height) and (x2, -height); accumulate totalArea.
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
        if (rectangles == null || rectangles.length == 0) {
            return 0;
        }

        // Each event is {x, heightChange}.
        double[][] events = new double[rectangles.length * 2][];
        double totalArea = 0;
        int next = 0;

        for (int[] rect : rectangles) {
            int startX = rect[0];
            int endX = rect[2];
            int height = rect[3] - rect[1];

            totalArea += (double) (endX - startX) * height;
            events[next++] = new double[] {startX, height};
            events[next++] = new double[] {endX, -height};
        }

        Arrays.sort(events, (a, b) -> Double.compare(a[0], b[0]));

        double targetArea = totalArea / 2.0;
        double currentArea = 0;
        double activeHeight = 0;
        double previousX = events[0][0];

        int eventIndex = 0;
        while (eventIndex < events.length) {
            // No rectangle starts or ends between these two x-coordinates,
            // so their combined height is constant throughout this strip.
            double stripStartX = previousX;
            double stripEndX = events[eventIndex][0];
            double stripWidth = stripEndX - stripStartX;
            double areaInStrip = stripWidth * activeHeight;

            // If half the total area falls inside this strip, determine how
            // far into the strip we must travel to collect the missing area.
            if (activeHeight > 0
                    && currentArea + areaInStrip >= targetArea) {
                double remainingAreaNeeded = targetArea - currentArea;
                double distanceIntoStrip = remainingAreaNeeded / activeHeight;
                return stripStartX + distanceIntoStrip;
            }

            // The cut was not in this strip, so include its entire area.
            currentArea += areaInStrip;

            // Apply every rectangle start/end event at this boundary. The
            // resulting height will be used for the next strip.
            while (eventIndex < events.length
                    && events[eventIndex][0] == stripEndX) {
                activeHeight += events[eventIndex][1];
                eventIndex++;
            }

            previousX = stripEndX;
        }

        return previousX;
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
