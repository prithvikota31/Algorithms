import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * ============================================================================
 * Problem 21 Follow-up 1 (Google L4 prep) - Make hasRectangle() O(1).
 * ============================================================================
 *
 * PROMPT
 *   Same add/query API as RectangleExists.java, but now hasRectangle() is
 *   called far more often than addPoint(). Push the work into addPoint()
 *   so hasRectangle() becomes a plain O(1) lookup.
 *
 * EXAMPLE
 *   addPoint(1,2), addPoint(1,5), addPoint(3,2), addPoint(3,5)
 *   -> hasRectangle() = true (as soon as the 4th point is added).
 *
 * INTUITION
 *   A rectangle needs two vertical lines (two x-columns) that each contain
 *   the SAME pair of y-values. So instead of grouping by x and comparing
 *   columns at query time, track every "vertical y-pair" seen at each x as
 *   points are added: yPairToXs[(y1,y2)] = set of x's having both y1 and y2.
 *   The moment a y-pair is seen at a SECOND distinct x, those two columns
 *   share two heights -- a rectangle exists. Flip a boolean flag right then
 *   and hasRectangle() just returns it.
 *
 * ALGORITHM
 *   1. addPoint(x, y):
 *        - for every y-value (oldY) already stored at this x, form the
 *          pair (min(oldY,y), max(oldY,y)) and record x under that pair's
 *          x-set.
 *        - if that pair's x-set now has >= 2 x's, set rectangleExists.
 *        - finally add y to this x's y-set.
 *   2. hasRectangle(): return the cached rectangleExists flag.
 *
 * COMPLEXITY
 *   Let K = number of y-values already at x when addPoint(x, y) is called.
 *   Time:  addPoint       -- O(K) (amortized cost shifts from query to insert).
 *          hasRectangle() -- O(1).
 *   Space: O(N + number of distinct y-pairs).
 * ============================================================================
 */
public class RectangleExistsIncremental {

    private final Map<Integer, Set<Integer>> xToYs = new HashMap<>();
    private final Map<String, Set<Integer>> yPairToXs = new HashMap<>();
    private boolean rectangleExists = false;

    public void addPoint(int x, int y) {
        if (xToYs.containsKey(x)) {
            for (int oldY : xToYs.get(x)) {
                if (oldY == y) {
                    continue; // duplicate point re-added, not a real height pair
                }
                int low = Math.min(oldY, y);
                int high = Math.max(oldY, y);
                String key = encode(low, high);

                yPairToXs.computeIfAbsent(key, k -> new HashSet<>()).add(x);

                if (yPairToXs.get(key).size() >= 2) {
                    rectangleExists = true;
                }
            }
        }

        xToYs.computeIfAbsent(x, k -> new HashSet<>()).add(y);
    }

    public boolean hasRectangle() {
        return rectangleExists;
    }

    private String encode(int y1, int y2) {
        return y1 + "#" + y2;
    }

    public static void main(String[] args) {
        // Full rectangle: (1,2),(1,5),(3,2),(3,5) -> true.
        RectangleExistsIncremental rect = new RectangleExistsIncremental();
        rect.addPoint(1, 2);
        rect.addPoint(1, 5);
        rect.addPoint(3, 2);
        rect.addPoint(3, 5);
        check("full rectangle", rect.hasRectangle(), true);

        // "L" shape missing one corner -> false.
        RectangleExistsIncremental lShape = new RectangleExistsIncremental();
        lShape.addPoint(1, 1);
        lShape.addPoint(1, 3);
        lShape.addPoint(3, 1);
        check("missing one corner", lShape.hasRectangle(), false);

        // Two columns share only 1 common y -> false.
        RectangleExistsIncremental oneShared = new RectangleExistsIncremental();
        oneShared.addPoint(1, 1);
        oneShared.addPoint(1, 5);
        oneShared.addPoint(4, 1);
        oneShared.addPoint(4, 9);
        check("only one shared y-value", oneShared.hasRectangle(), false);

        // Rectangle formed by non-adjacent points, with extra noise points.
        RectangleExistsIncremental threeCols = new RectangleExistsIncremental();
        threeCols.addPoint(1, 2);
        threeCols.addPoint(1, 4);
        threeCols.addPoint(2, 2);
        threeCols.addPoint(5, 2);
        threeCols.addPoint(5, 4);
        check("rectangle across non-adjacent columns", threeCols.hasRectangle(), true);

        // Single point / empty plane -> false.
        RectangleExistsIncremental single = new RectangleExistsIncremental();
        single.addPoint(1, 1);
        check("single point", single.hasRectangle(), false);
        check("empty plane", new RectangleExistsIncremental().hasRectangle(), false);

        // Duplicate point re-added at two columns must not create a false rectangle.
        RectangleExistsIncremental duplicatePoints = new RectangleExistsIncremental();
        duplicatePoints.addPoint(1, 2);
        duplicatePoints.addPoint(1, 2);
        duplicatePoints.addPoint(3, 2);
        duplicatePoints.addPoint(3, 2);
        check("duplicate points at two columns", duplicatePoints.hasRectangle(), false);

        System.out.println("all passed");
    }

    private static void check(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual
                    + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
