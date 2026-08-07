import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * ============================================================================
 * Problem 22 (Google L4 prep) - Vertical line splitting rectangle area equally.
 * ============================================================================
 *
 * PROMPT
 *   Given a set of axis-aligned rectangles, find a vertical line x = k such
 *   that the total rectangle area to the left of the line equals the total
 *   area to the right.
 *
 *   Overlapping rectangle area is counted separately. Each rectangle always
 *   contributes its full area, even where it overlaps another rectangle.
 *
 * EXAMPLE
 *   Rectangles [0,0,2,2] and [2,0,4,2] each have area 4, so the cut is x = 2.
 *   Rectangles [0,0,4,2] and [0,0,2,2] have total area 12. Their combined
 *   active height is 4 from x = 0 to x = 2, so the cut is x = 1.5.
 *
 * INTUITION
 *   Turn each rectangle into two x-events: add its height at x1 and remove
 *   its height at x2. Between consecutive event coordinates, the sum of
 *   active heights is constant, so area grows linearly at that rate. Once
 *   the target falls in a strip, solve directly for the exact x-coordinate.
 *
 * ALGORITHM
 *   1. Add events (x1, +height) and (x2, -height) for every rectangle while
 *      summing their areas independently.
 *   2. Sort events by x and set targetArea = totalArea / 2.
 *   3. Sweep each strip using stripArea = width * activeHeight.
 *   4. If the target lies in the strip, divide the remaining area by
 *      activeHeight to find the exact x; otherwise process all events at x.
 *
 * COMPLEXITY
 *   Time:  O(N log N) to sort the 2N events.
 *   Space: O(N) for the events.
 *
 * FOLLOW-UPS
 *   Binary search on x is a simpler O(N log precision) alternative: scan all
 *   rectangles to measure left area for each guess. If overlapping area must
 *   count only once, the sweep must track union length of active y-intervals.
 * ============================================================================
 */
public class VerticalAreaSplit {

    private static class Event {
        final double x;
        final double heightChange;

        Event(double x, double heightChange) {
            this.x = x;
            this.heightChange = heightChange;
        }
    }

    public double findVerticalCut(int[][] rectangles) {
        if (rectangles == null || rectangles.length == 0) {
            return 0;
        }

        List<Event> events = new ArrayList<>(rectangles.length * 2);
        double totalArea = 0;
        for (int[] rect : rectangles) {
            double width = (double) rect[2] - rect[0];
            double height = (double) rect[3] - rect[1];

            totalArea += width * height;
            events.add(new Event(rect[0], height));
            events.add(new Event(rect[2], -height));
        }

        events.sort(Comparator.comparingDouble(event -> event.x));

        double targetArea = totalArea / 2.0;
        double currentArea = 0;
        double activeHeight = 0;
        double previousX = events.get(0).x;

        int eventIndex = 0;
        while (eventIndex < events.size()) {
            double currentX = events.get(eventIndex).x;
            double stripArea = (currentX - previousX) * activeHeight;

            if (activeHeight > 0 && currentArea + stripArea >= targetArea) {
                return previousX + (targetArea - currentArea) / activeHeight;
            }

            currentArea += stripArea;
            while (eventIndex < events.size()
                    && Double.compare(events.get(eventIndex).x, currentX) == 0) {
                activeHeight += events.get(eventIndex).heightChange;
                eventIndex++;
            }
            previousX = currentX;
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

        // Shared x-range: both rectangle heights contribute to the area rate.
        check("overlapping rectangles count separately", sol.findVerticalCut(new int[][] {
            {0, 0, 4, 2}, {0, 0, 2, 2}
        }), 1.5);

        // Overlapping x-projections also work when the y-ranges are disjoint.
        check("stacked rectangles", sol.findVerticalCut(new int[][] {
            {0, 0, 4, 2}, {0, 5, 2, 7}
        }), 1.5);

        // Unsorted input -- sort step must still find the correct cut.
        check("unsorted input", sol.findVerticalCut(new int[][] {
                {4, 0, 6, 4}, {0, 0, 4, 2}
        }), 4.0);

        // Rectangles with a gap between them; gap contributes zero area.
        check("gap between rectangles", sol.findVerticalCut(new int[][] {
                {0, 0, 2, 2}, {5, 0, 7, 2}
        }), 2.0);

        System.out.println("all passed");
    }

    private static void check(String name, double actual, double expected) {
        if (Math.abs(actual - expected) > 1e-9) {
            throw new AssertionError("FAIL " + name + ": got " + actual
                    + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
