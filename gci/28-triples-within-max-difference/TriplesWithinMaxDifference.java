import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * ============================================================================
 * Problem 28 (Google L4 prep) — Triples From Three Sorted Arrays Within D
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given three SORTED arrays A, B, C, count index triples (i, j, k) such that
 * every pairwise difference between A[i], B[j], C[k] is at most D.
 *
 * Key simplification: max(A[i],B[j],C[k]) - min(A[i],B[j],C[k]) <= D is
 * EQUIVALENT to every pairwise difference being <= D (every value lies
 * within [min, min+D], so any two of them differ by at most D).
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   We count INDEX triples, not distinct value triples: A=[5,5], B=[5],
 *   C=[6], D=1 has 2 valid triples (one per occurrence of 5 in A). Use
 *   `long` for the count — it can be as large as a.length*b.length*c.length.
 *
 * EXAMPLE
 *   A=[1,5], B=[3,5], C=[6,8], D=1  ->  only (A[1]=5, B[1]=5, C[0]=6) is
 *   valid (max 6 - min 5 = 1 <= D)  ->  answer = 1
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: DESIGNATED-MINIMUM ANCHOR + BINARY-SEARCH RANGE COUNTING.
 *
 * Fix one array's value x as the triple's minimum. The other two values must
 * then lie in [x, x+D] (both arrays are sorted, so that's a contiguous range
 * found via binary search: count = upperBound(x+D) - lowerBound(x)).
 *
 * PROBLEM: multiple arrays can tie at the same minimum value, which would
 * double-count the same value combination as "anchored by A" AND "anchored
 * by B". Fix with a tie-break priority order A before B before C:
 *   - A is the anchor  -> B, C may be EQUAL to it        (range [x, x+D])
 *   - B is the anchor  -> A must be STRICTLY greater,     C may be equal
 *                         (A range (x, x+D], C range [x, x+D])
 *   - C is the anchor  -> A and B must both be STRICTLY greater
 *                         (both ranges (x, x+D])
 * Every valid triple is now anchored by exactly one array (whichever of its
 * three values comes first in A > B > C priority among the tied minima).
 *
 * APPROACHES
 *   Brute force : try every (i, j, k) triple directly. O(a*b*c) time.
 *   Optimal     : for each value, binary-search the valid range in the other
 *                 two arrays (below). O((a+b+c) log max(a,b,c)) time.
 *
 * COMPLEXITY
 *   Time O((a+b+c) log(max(a,b,c)))   Space O(1) beyond the output
 * ----------------------------------------------------------------------------
 */
public class TriplesWithinMaxDifference {

    public long countValidTriples(int[] a, int[] b, int[] c, int maxDifference) {
        if (a == null || b == null || c == null
                || a.length == 0 || b.length == 0 || c.length == 0
                || maxDifference < 0) {
            return 0;
        }

        long total = 0;

        // A anchors the minimum: B and C may equal it.
        for (int valueA : a) {
            long hi = (long) valueA + maxDifference;
            total += countClosed(b, valueA, hi) * countClosed(c, valueA, hi);
        }

        // B anchors the minimum: A must be strictly greater (else it was
        // already counted under A), C may still equal it.
        for (int valueB : b) {
            long hi = (long) valueB + maxDifference;
            total += countOpenClosed(a, valueB, hi) * countClosed(c, valueB, hi);
        }

        // C anchors the minimum: both A and B must be strictly greater.
        for (int valueC : c) {
            long hi = (long) valueC + maxDifference;
            total += countOpenClosed(a, valueC, hi) * countOpenClosed(b, valueC, hi);
        }

        return total;
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): return the actual triples, not just the count.
     *
     * MENTAL MAP
     *   Same three-pass anchor scan, but instead of multiplying range sizes,
     *   walk both ranges and emit every (i, j, k) combination.
     * ------------------------------------------------------------------------
     */
    public List<int[]> findValidTriples(int[] a, int[] b, int[] c, int maxDifference) {
        List<int[]> triples = new ArrayList<>();
        if (a == null || b == null || c == null
                || a.length == 0 || b.length == 0 || c.length == 0
                || maxDifference < 0) {
            return triples;
        }

        for (int i = 0; i < a.length; i++) {
            long hi = (long) a[i] + maxDifference;
            int bLo = lowerBound(b, a[i]);
            int bHi = upperBound(b, hi);
            int cLo = lowerBound(c, a[i]);
            int cHi = upperBound(c, hi);
            for (int j = bLo; j < bHi; j++) {
                for (int k = cLo; k < cHi; k++) {
                    triples.add(new int[] { i, j, k });
                }
            }
        }

        for (int j = 0; j < b.length; j++) {
            long hi = (long) b[j] + maxDifference;
            int aLo = upperBound(a, b[j]);
            int aHi = upperBound(a, hi);
            int cLo = lowerBound(c, b[j]);
            int cHi = upperBound(c, hi);
            for (int i = aLo; i < aHi; i++) {
                for (int k = cLo; k < cHi; k++) {
                    triples.add(new int[] { i, j, k });
                }
            }
        }

        for (int k = 0; k < c.length; k++) {
            long hi = (long) c[k] + maxDifference;
            int aLo = upperBound(a, c[k]);
            int aHi = upperBound(a, hi);
            int bLo = upperBound(b, c[k]);
            int bHi = upperBound(b, hi);
            for (int i = aLo; i < aHi; i++) {
                for (int j = bLo; j < bHi; j++) {
                    triples.add(new int[] { i, j, k });
                }
            }
        }

        return triples;
    }

    // Counts values satisfying low <= value <= high.
    private static long countClosed(int[] array, long low, long high) {
        return upperBound(array, high) - lowerBound(array, low);
    }

    // Counts values satisfying low < value <= high.
    private static long countOpenClosed(int[] array, long low, long high) {
        return upperBound(array, high) - upperBound(array, low);
    }

    // Returns the first index whose value is >= target.
    private static int lowerBound(int[] array, long target) {
        int left = 0;
        int right = array.length;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (array[middle] >= target) {
                right = middle;
            } else {
                left = middle + 1;
            }
        }
        return left;
    }

    // Returns the first index whose value is > target.
    private static int upperBound(int[] array, long target) {
        int left = 0;
        int right = array.length;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (array[middle] > target) {
                right = middle;
            } else {
                left = middle + 1;
            }
        }
        return left;
    }

    public static void main(String[] args) {
        TriplesWithinMaxDifference solution = new TriplesWithinMaxDifference();

        check("worked example", solution.countValidTriples(
                new int[] { 1, 5 }, new int[] { 3, 5 }, new int[] { 6, 8 }, 1), 1L);
        check("duplicate values across arrays", solution.countValidTriples(
                new int[] { 5, 5 }, new int[] { 5 }, new int[] { 6 }, 1), 2L);
        check("no valid triples", solution.countValidTriples(
                new int[] { 1 }, new int[] { 100 }, new int[] { 200 }, 5), 0L);
        check("D=0 exact match", solution.countValidTriples(
                new int[] { 3 }, new int[] { 3 }, new int[] { 3 }, 0), 1L);
        check("all combinations valid", solution.countValidTriples(
                new int[] { 1, 2 }, new int[] { 1, 2 }, new int[] { 1, 2 }, 10), 8L);
        check("empty array", solution.countValidTriples(
                new int[] {}, new int[] { 1 }, new int[] { 1 }, 1), 0L);

        check("worked example triples", solution.findValidTriples(
                new int[] { 1, 5 }, new int[] { 3, 5 }, new int[] { 6, 8 }, 1),
                List.of(new int[] { 1, 1, 0 }));

        System.out.println("all passed");
    }

    private static void check(String name, long actual, long expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }

    private static void check(String name, List<int[]> actual, List<int[]> expected) {
        boolean same = actual.size() == expected.size();
        for (int idx = 0; same && idx < actual.size(); idx++) {
            same = Arrays.equals(actual.get(idx), expected.get(idx));
        }
        if (!same) {
            throw new AssertionError("FAIL " + name + ": got " + toString(actual)
                    + " want " + toString(expected));
        }
        System.out.println("pass " + name + " -> " + toString(actual));
    }

    private static String toString(List<int[]> triples) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < triples.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(Arrays.toString(triples.get(i)));
        }
        return sb.append("]").toString();
    }
}
