import java.util.Arrays;

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
 * ASSUMPTION (important -- narrower than general "no overlap")
 *   Rectangles do not overlap in AREA and also do not overlap in their
 *   x-projections -- i.e. they are laid out strictly left-to-right (may have
 *   gaps between them, but never share any x-range). This lets us process
 *   rectangles one at a time, left to right, and treat each one's area as
 *   accruing linearly across its own width in isolation.
 *   (If rectangles could share an x-range while stacked at different
 *   heights -- non-overlapping area but overlapping x-projection -- this
 *   single-pass approach breaks; that needs a sweep-line summing active
 *   heights per x-interval instead.)
 *
 * EXAMPLE
 *   Rectangle [0,0,4,2] (area 8) alone -> half = 4 -> cut at x = 2.
 *   Rectangle A [0,0,4,2] (area 8) + B [4,0,6,4] (area 8) -> total 16,
 *   half = 8 = area of A exactly -> cut at x = 4 (right at the boundary).
 *
 * INTUITION
 *   Sort rectangles by startX. Sweep left to right, accumulating area.
 *   The moment the running total would reach/exceed half the total area
 *   inside the current rectangle, that rectangle's own area is added
 *   linearly with x (since width contributes proportionally at fixed
 *   height), so solve for the exact x directly: remaining area needed
 *   divided by height gives the extra width past this rectangle's startX.
 *
 * ALGORITHM
 *   1. Sort rectangles by x1 (startX).
 *   2. Compute totalArea = sum of width * height over all rectangles.
 *   3. targetArea = totalArea / 2.
 *   4. Scan sorted rectangles, tracking areaSoFar:
 *        - if areaSoFar + thisRectArea >= targetArea, the cut is inside
 *          this rectangle: answer = startX + (targetArea - areaSoFar) / height.
 *        - otherwise add thisRectArea to areaSoFar and continue.
 *
 * COMPLEXITY
 *   Time:  O(N log N) for the sort, O(N) for the scan.
 *   Space: O(1) extra (sort may use O(log N) internally).
 * ============================================================================
 */
public class VerticalAreaSplit {

    public double findVerticalCut(int[][] rectangles) {
        int n = rectangles.length;
        if (n == 0) {
            return 0;
        }

        Arrays.sort(rectangles, (a, b) -> Integer.compare(a[0], b[0]));

        double totalArea = 0;
        for (int[] rect : rectangles) {
            int width = rect[2] - rect[0];
            int height = rect[3] - rect[1];
            totalArea += (double) width * height;
        }

        double targetArea = totalArea / 2.0;

        double areaSoFar = 0;
        for (int[] rect : rectangles) {
            int startX = rect[0];
            int endX = rect[2];
            int height = rect[3] - rect[1];

            double rectangleArea = (double) (endX - startX) * height;

            if (areaSoFar + rectangleArea >= targetArea) {
                double remainingArea = targetArea - areaSoFar;
                double neededWidth = remainingArea / height;
                return startX + neededWidth;
            }

            areaSoFar += rectangleArea;
        }

        return -1;
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
