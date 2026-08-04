import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * ============================================================================
 * Problem 21 Follow-up 2 (Google L4 prep) - Maximum rectangle area.
 * ============================================================================
 *
 * PROMPT
 *   Same incremental addPoint(x, y) API, but instead of a boolean, return
 *   the area of the LARGEST axis-aligned rectangle that can be formed from
 *   the points added so far.
 *
 * EXAMPLE
 *   addPoint(1,1), addPoint(5,1), addPoint(1,4), addPoint(5,4)
 *   -> getMaxArea() = |5-1| * |4-1| = 4 * 3 = 12.
 *
 * INTUITION
 *   Same vertical-pair idea as follow-up 1 (hasRectangle in O(1)), but now
 *   every time a y-pair (y1,y2) is seen at a NEW x, that x forms a
 *   rectangle with EVERY previous x that also had this exact y-pair --
 *   width = |x - previousX|, height = y2 - y1. Track the max area seen
 *   across all such comparisons instead of just flipping a boolean.
 *
 * ALGORITHM
 *   1. addPoint(x, y):
 *        - for every y-value (oldY) already stored at this x, form the
 *          pair (min(oldY,y), max(oldY,y)).
 *        - for every previousX already recorded under that pair, compute
 *          area = |x - previousX| * height and update maxArea.
 *        - record x under that pair's x-list.
 *        - finally add y to this x's y-set.
 *   2. getMaxArea(): return the cached maxArea.
 *
 * COMPLEXITY
 *   Let K = number of y-values already at x, X = number of previous x's
 *   sharing a given y-pair.
 *   Time:  addPoint     -- O(K * X) worst case.
 *          getMaxArea() -- O(1).
 *   Space: O(N + number of distinct y-pairs).
 * ============================================================================
 */
public class MaxRectangleArea {

    private final Map<Integer, Set<Integer>> xToYs = new HashMap<>();
    private final Map<String, List<Integer>> yPairToXs = new HashMap<>();
    private int maxArea = 0;

    public void addPoint(int x, int y) {
        if (xToYs.containsKey(x)) {
            for (int oldY : xToYs.get(x)) {
                int y1 = Math.min(oldY, y);
                int y2 = Math.max(oldY, y);
                String key = encode(y1, y2);
                int height = y2 - y1;

                if (yPairToXs.containsKey(key)) {
                    for (int previousX : yPairToXs.get(key)) {
                        int width = Math.abs(x - previousX);
                        maxArea = Math.max(maxArea, width * height);
                    }
                }

                yPairToXs.computeIfAbsent(key, k -> new ArrayList<>()).add(x);
            }
        }

        xToYs.computeIfAbsent(x, k -> new HashSet<>()).add(y);
    }

    public int getMaxArea() {
        return maxArea;
    }

    private String encode(int y1, int y2) {
        return y1 + "," + y2;
    }

    public static void main(String[] args) {
        // Basic rectangle: (1,1),(5,1),(1,4),(5,4) -> area 12.
        MaxRectangleArea rect = new MaxRectangleArea();
        rect.addPoint(1, 1);
        rect.addPoint(5, 1);
        rect.addPoint(1, 4);
        rect.addPoint(5, 4);
        check("basic rectangle", rect.getMaxArea(), 12);

        // Three columns sharing the same y-pair -- max area uses the
        // farthest-apart pair (x=1 and x=10), not just the latest one.
        MaxRectangleArea threeCols = new MaxRectangleArea();
        threeCols.addPoint(1, 2);
        threeCols.addPoint(1, 7);
        threeCols.addPoint(5, 2);
        threeCols.addPoint(5, 7);
        threeCols.addPoint(10, 2);
        threeCols.addPoint(10, 7);
        check("farthest columns win", threeCols.getMaxArea(), 45);

        // No shared y-pair across any two columns -> no rectangle, area 0.
        MaxRectangleArea noRect = new MaxRectangleArea();
        noRect.addPoint(1, 1);
        noRect.addPoint(1, 3);
        noRect.addPoint(3, 1);
        check("no rectangle formed", noRect.getMaxArea(), 0);

        // Multiple y-pairs at one x -- only the pair that repeats elsewhere counts.
        MaxRectangleArea multiPair = new MaxRectangleArea();
        multiPair.addPoint(1, 1);
        multiPair.addPoint(1, 2);
        multiPair.addPoint(1, 10);
        multiPair.addPoint(4, 1);
        multiPair.addPoint(4, 2);
        check("only matching pair counts", multiPair.getMaxArea(), 3);

        // Duplicate point re-added at two columns contributes area 0 (height 0),
        // so it can never corrupt a real max -- unlike the boolean hasRectangle().
        MaxRectangleArea duplicatePoints = new MaxRectangleArea();
        duplicatePoints.addPoint(1, 2);
        duplicatePoints.addPoint(1, 2);
        duplicatePoints.addPoint(3, 2);
        duplicatePoints.addPoint(3, 2);
        check("duplicate points contribute zero area", duplicatePoints.getMaxArea(), 0);

        // Single point / empty plane -> area 0.
        MaxRectangleArea single = new MaxRectangleArea();
        single.addPoint(1, 1);
        check("single point", single.getMaxArea(), 0);
        check("empty plane", new MaxRectangleArea().getMaxArea(), 0);

        // Alternate (batch, diagonal-pairs) approach -- same scenarios, same results.
        check("batch: basic rectangle", MaxRectangleAreaBatch.maxArea(List.of(
                new int[] {1, 1}, new int[] {5, 1}, new int[] {1, 4}, new int[] {5, 4})), 12);
        check("batch: farthest columns win", MaxRectangleAreaBatch.maxArea(List.of(
                new int[] {1, 2}, new int[] {1, 7}, new int[] {5, 2}, new int[] {5, 7},
                new int[] {10, 2}, new int[] {10, 7})), 45);
        check("batch: no rectangle formed", MaxRectangleAreaBatch.maxArea(List.of(
                new int[] {1, 1}, new int[] {1, 3}, new int[] {3, 1})), 0);

        System.out.println("all passed");
    }

    private static void check(String name, int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual
                    + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}

/*
 * ALTERNATE APPROACH -- batch/static version using diagonal-corner pairs
 * (same idea as RectangleExists.java), for when all points are known
 * upfront instead of arriving one at a time via addPoint().
 *
 * For every pair of points as a candidate diagonal (x1,y1)-(x2,y2), skip if
 * they share an x or y, then check whether the other two corners (x1,y2)
 * and (x2,y1) exist. If so, area = |x2-x1| * |y2-y1|; track the max.
 * O(N^2) time, O(N) space -- same complexity class as the incremental
 * version, just simpler to reason about with a fixed point set.
 */
class MaxRectangleAreaBatch {

    public static int maxArea(List<int[]> points) {
        Set<String> pointSet = new HashSet<>();
        for (int[] p : points) {
            pointSet.add(encode(p[0], p[1]));
        }

        int maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                int x1 = points.get(i)[0];
                int y1 = points.get(i)[1];
                int x2 = points.get(j)[0];
                int y2 = points.get(j)[1];

                if (x1 == x2 || y1 == y2) {
                    continue;
                }

                if (pointSet.contains(encode(x1, y2)) && pointSet.contains(encode(x2, y1))) {
                    int area = Math.abs(x2 - x1) * Math.abs(y2 - y1);
                    maxArea = Math.max(maxArea, area);
                }
            }
        }

        return maxArea;
    }

    private static String encode(int x, int y) {
        return x + "," + y;
    }
}
