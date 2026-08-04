import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * ============================================================================
 * Problem 21 (Google L4 prep) - Detect an axis-aligned rectangle from points.
 * ============================================================================
 *
 * PROMPT
 *   Support two operations on a stream of 2D points:
 *     addPoint(x, y) -- add a point to the plane.
 *     hasRectangle() -- does any axis-aligned rectangle exist whose 4
 *                       corners are all points that have been added?
 *
 * EXAMPLE
 *   add(1,1), add(1,3), add(3,1), add(3,3) -> hasRectangle() = true.
 *
 * INTUITION
 *   A rectangle is uniquely determined by two OPPOSITE (diagonal) corners:
 *   (x1,y1) and (x2,y2) with x1 != x2 and y1 != y2. The other two corners
 *   are then forced to be (x1,y2) and (x2,y1). So: pick every pair of
 *   stored points as a candidate diagonal, and just check whether the two
 *   "missing" corners are also in the set.
 *
 * ALGORITHM
 *   1. addPoint(x, y): store the point in a hash set (encoded as "x,y").
 *   2. hasRectangle(): for every pair of distinct points (p1, p2):
 *        - skip if they share an x or a y (can't be a diagonal).
 *        - otherwise check if (x1,y2) and (x2,y1) are both in the set.
 *        - if yes, a rectangle exists.
 *
 * COMPLEXITY
 *   Time:  O(N^2) -- every pair of points checked, O(1) HashSet lookups.
 *   Space: O(N) -- one entry per distinct point.
 * ============================================================================
 */
public class RectangleExists {

    private final Set<String> points = new HashSet<>();

    public void addPoint(int x, int y) {
        points.add(encode(x, y));
    }

    public boolean hasRectangle() {
        List<int[]> pointList = new ArrayList<>();
        for (String point : points) {
            String[] parts = point.split(",");
            pointList.add(new int[] {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }

        for (int i = 0; i < pointList.size(); i++) {
            for (int j = i + 1; j < pointList.size(); j++) {
                int x1 = pointList.get(i)[0];
                int y1 = pointList.get(i)[1];
                int x2 = pointList.get(j)[0];
                int y2 = pointList.get(j)[1];

                // Same row or same column can't be diagonal corners.
                if (x1 == x2 || y1 == y2) {
                    continue;
                }

                if (points.contains(encode(x1, y2)) && points.contains(encode(x2, y1))) {
                    return true;
                }
            }
        }

        return false;
    }

    private String encode(int x, int y) {
        return x + "," + y;
    }

    public static void main(String[] args) {
        // Full rectangle: (1,1),(1,3),(3,1),(3,3) -> true.
        RectangleExists rect = new RectangleExists();
        rect.addPoint(1, 1);
        rect.addPoint(1, 3);
        rect.addPoint(3, 1);
        rect.addPoint(3, 3);
        check("full rectangle", rect.hasRectangle(), true);

        // "L" shape missing one corner -> false.
        RectangleExists lShape = new RectangleExists();
        lShape.addPoint(1, 1);
        lShape.addPoint(1, 3);
        lShape.addPoint(3, 1);
        check("missing one corner", lShape.hasRectangle(), false);

        // Two columns share only 1 common y -> false.
        RectangleExists oneShared = new RectangleExists();
        oneShared.addPoint(1, 1);
        oneShared.addPoint(1, 5);
        oneShared.addPoint(4, 1);
        oneShared.addPoint(4, 9);
        check("only one shared y-value", oneShared.hasRectangle(), false);

        // Rectangle formed by non-adjacent points, with extra noise points.
        RectangleExists threeCols = new RectangleExists();
        threeCols.addPoint(1, 2);
        threeCols.addPoint(1, 4);
        threeCols.addPoint(2, 2);
        threeCols.addPoint(5, 2);
        threeCols.addPoint(5, 4);
        check("rectangle across non-adjacent columns", threeCols.hasRectangle(), true);

        // Single point / empty plane -> false.
        RectangleExists single = new RectangleExists();
        single.addPoint(1, 1);
        check("single point", single.hasRectangle(), false);
        check("empty plane", new RectangleExists().hasRectangle(), false);

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
