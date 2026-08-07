import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================================
 * Problem 53 (Google L4 prep) - Count Squares from Line Segments
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given horizontal and vertical segments [x1, y1, x2, y2], count distinct
 * axis-aligned squares whose four sides are continuously covered. A side may
 * be assembled from multiple overlapping or touching segments.
 *
 * EXAMPLES
 * --------
 * Four segments forming the boundary of a 2-by-2 square -> 1
 * A 2-by-2 grid with every full row and column present -> 5
 * A candidate boundary with a gap in one side -> 0
 *
 * INTUITION
 * ---------
 * Group horizontal coverage by y and vertical coverage by x, then merge every
 * line's overlapping or touching intervals. A square is fixed by its left and
 * right x coordinates plus its bottom y coordinate; the side length determines
 * the top y. It exists exactly when four merged-interval queries succeed.
 *
 * ALGORITHM
 * ---------
 * 1. Normalize and group horizontal x-intervals by y and vertical y-intervals
 *    by x.
 * 2. Merge overlapping or touching intervals on every line.
 * 3. Enumerate each pair of vertical x coordinates as the left and right sides.
 * 4. For every horizontal level y1, derive y2 = y1 + (x2 - x1).
 * 5. Count the candidate if all four required intervals are covered.
 *
 * COMPLEXITY
 * ----------
 * Time: O(N log N + X^2 * Y * log N), where N is the segment count, X is the
 *       number of vertical lines, and Y is the number of horizontal lines.
 * Space: O(N).
 * ============================================================================
 */
public class CountSquaresFromSegments {

    public long countSquares(int[][] segments) {
        if (segments == null || segments.length == 0) {
            return 0;
        }

        Map<Integer, List<int[]>> horizontal = new HashMap<>();
        Map<Integer, List<int[]>> vertical = new HashMap<>();

        for (int[] segment : segments) {
            if (segment == null || segment.length < 4) {
                throw new IllegalArgumentException("each segment must contain four coordinates");
            }

            int x1 = segment[0];
            int y1 = segment[1];
            int x2 = segment[2];
            int y2 = segment[3];

            if (x1 == x2 && y1 == y2) {
                continue;
            }
            if (y1 == y2) {
                horizontal.computeIfAbsent(y1, ignored -> new ArrayList<>())
                        .add(new int[] { Math.min(x1, x2), Math.max(x1, x2) });
            } else if (x1 == x2) {
                vertical.computeIfAbsent(x1, ignored -> new ArrayList<>())
                        .add(new int[] { Math.min(y1, y2), Math.max(y1, y2) });
            } else {
                throw new IllegalArgumentException("segments must be horizontal or vertical");
            }
        }

        mergeEveryLine(horizontal);
        mergeEveryLine(vertical);

        List<Integer> verticalCoordinates = new ArrayList<>(vertical.keySet());
        Collections.sort(verticalCoordinates);
        List<Integer> horizontalCoordinates = new ArrayList<>(horizontal.keySet());

        long squareCount = 0;

        for (int leftIndex = 0; leftIndex < verticalCoordinates.size(); leftIndex++) {
            int leftX = verticalCoordinates.get(leftIndex);

            for (int rightIndex = leftIndex + 1; rightIndex < verticalCoordinates.size(); rightIndex++) {
                int rightX = verticalCoordinates.get(rightIndex);
                long sideLength = (long) rightX - leftX;

                for (int bottomY : horizontalCoordinates) {
                    long topYLong = bottomY + sideLength;
                    if (topYLong < Integer.MIN_VALUE || topYLong > Integer.MAX_VALUE) {
                        continue;
                    }

                    int topY = (int) topYLong;
                    if (covers(horizontal.get(bottomY), leftX, rightX)
                            && covers(horizontal.get(topY), leftX, rightX)
                            && covers(vertical.get(leftX), bottomY, topY)
                            && covers(vertical.get(rightX), bottomY, topY)) {
                        squareCount++;
                    }
                }
            }
        }

        return squareCount;
    }

    private static void mergeEveryLine(Map<Integer, List<int[]>> coverageByCoordinate) {
        for (Map.Entry<Integer, List<int[]>> entry : coverageByCoordinate.entrySet()) {
            entry.setValue(merge(entry.getValue()));
        }
    }

    private static List<int[]> merge(List<int[]> intervals) {
        intervals.sort(Comparator.comparingInt(interval -> interval[0]));
        List<int[]> merged = new ArrayList<>();

        for (int[] interval : intervals) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(new int[] { interval[0], interval[1] });
            } else {
                int[] previous = merged.get(merged.size() - 1);
                previous[1] = Math.max(previous[1], interval[1]);
            }
        }

        return merged;
    }

    private static boolean covers(List<int[]> intervals, int start, int end) {
        if (intervals == null) {
            return false;
        }

        int low = 0;
        int high = intervals.size() - 1;
        int candidateIndex = -1;

        while (low <= high) {
            int middle = low + (high - low) / 2;
            if (intervals.get(middle)[0] <= start) {
                candidateIndex = middle;
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }

        return candidateIndex >= 0 && intervals.get(candidateIndex)[1] >= end;
    }

    public static void main(String[] args) {
        CountSquaresFromSegments solution = new CountSquaresFromSegments();

        check("touching segments form one square", solution.countSquares(new int[][] {
                { 0, 0, 1, 0 }, { 1, 0, 2, 0 },
                { 0, 2, 2, 2 },
                { 0, 0, 0, 1 }, { 0, 1, 0, 2 },
                { 2, 0, 2, 2 }
        }), 1L);

        check("two-by-two grid", solution.countSquares(new int[][] {
                { 0, 0, 2, 0 }, { 0, 1, 2, 1 }, { 0, 2, 2, 2 },
                { 0, 0, 0, 2 }, { 1, 0, 1, 2 }, { 2, 0, 2, 2 }
        }), 5L);

        check("gap prevents square", solution.countSquares(new int[][] {
                { 0, 0, 1, 0 }, { 2, 0, 3, 0 }, { 0, 3, 3, 3 },
                { 0, 0, 0, 3 }, { 3, 0, 3, 3 }
        }), 0L);

        check("reversed and overlapping segments", solution.countSquares(new int[][] {
                { 3, 0, 1, 0 }, { 0, 0, 2, 0 },
                { 3, 3, 0, 3 },
                { 0, 3, 0, 0 }, { 3, 3, 3, 0 }
        }), 1L);

        check("empty input", solution.countSquares(new int[0][]), 0L);
        System.out.println("all passed");
    }

    private static void check(String name, long actual, long expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}