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
 * Pattern: DESIGNATED-MINIMUM ANCHOR + MONOTONIC TWO-POINTER RANGE COUNTING.
 *
 * Fix one array's value x as the triple's minimum. The other two values must
 * then lie in [x, x+D]. Because ALL THREE arrays are sorted, as x increases
 * while scanning the anchor array left to right, both range boundaries (x
 * and x+D) only increase too — so the pointers marking "first index >= x"
 * and "first index > x+D" in the other arrays only ever move FORWARD across
 * the whole scan. No binary search needed: each pointer crosses its array at
 * most once in total, giving O(arrayLength) per pass instead of O(log n)
 * per anchor.
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
 *   Brute force  : try every (i, j, k) triple directly. O(a*b*c) time.
 *   Binary search: per anchor value, binary-search the valid range in the
 *                  other two arrays. O((a+b+c) log max(a,b,c)) time — a
 *                  simpler fallback if the two-pointer bookkeeping is fuzzy.
 *   Optimal      : three synchronized monotonic pointers (below), since the
 *                  anchor scan AND the range bounds are both non-decreasing.
 *                  O(a+b+c) time.
 *
 * COMPLEXITY
 *   Time O(a + b + c)   Space O(1) beyond the output
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

        // A anchors the minimum: B and C may equal it. bLo/cLo mark the
        // first index >= the anchor; bHi/cHi mark the first index > the
        // anchor + D. All four only move forward as `a[i]` increases.
        int bLo = 0, bHi = 0, cLo = 0, cHi = 0;
        for (int valueA : a) {
            long hi = (long) valueA + maxDifference;
            while (bLo < b.length && b[bLo] < valueA) bLo++;
            while (bHi < b.length && b[bHi] <= hi) bHi++;
            while (cLo < c.length && c[cLo] < valueA) cLo++;
            while (cHi < c.length && c[cHi] <= hi) cHi++;
            total += (long) Math.max(0, bHi - bLo) * Math.max(0, cHi - cLo);
        }

        // B anchors the minimum: A must be strictly greater (else it was
        // already counted under A), C may still equal it.
        int aLo = 0, aHi = 0;
        cLo = 0;
        cHi = 0;
        for (int valueB : b) {
            long hi = (long) valueB + maxDifference;
            while (aLo < a.length && a[aLo] <= valueB) aLo++;
            while (aHi < a.length && a[aHi] <= hi) aHi++;
            while (cLo < c.length && c[cLo] < valueB) cLo++;
            while (cHi < c.length && c[cHi] <= hi) cHi++;
            total += (long) Math.max(0, aHi - aLo) * Math.max(0, cHi - cLo);
        }

        // C anchors the minimum: both A and B must be strictly greater.
        aLo = 0;
        aHi = 0;
        bLo = 0;
        bHi = 0;
        for (int valueC : c) {
            long hi = (long) valueC + maxDifference;
            while (aLo < a.length && a[aLo] <= valueC) aLo++;
            while (aHi < a.length && a[aHi] <= hi) aHi++;
            while (bLo < b.length && b[bLo] <= valueC) bLo++;
            while (bHi < b.length && b[bHi] <= hi) bHi++;
            total += (long) Math.max(0, aHi - aLo) * Math.max(0, bHi - bLo);
        }

        return total;
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): return the actual triples, not just the count.
     * Output size itself can be huge (up to a.length*b.length*c.length), so
     * this is inherently O(output size) on top of the O(a+b+c) pointer scan.
     *
     * MENTAL MAP
     *   Same three-pass monotonic-pointer scan, but instead of multiplying
     *   range sizes, walk both ranges and emit every (i, j, k) combination.
     * ------------------------------------------------------------------------
     */
    public List<int[]> findValidTriples(int[] a, int[] b, int[] c, int maxDifference) {
        List<int[]> triples = new ArrayList<>();
        if (a == null || b == null || c == null
                || a.length == 0 || b.length == 0 || c.length == 0
                || maxDifference < 0) {
            return triples;
        }

        // A anchors the minimum: B and C may equal it.
        int bLo = 0, bHi = 0, cLo = 0, cHi = 0;
        for (int i = 0; i < a.length; i++) {
            long hi = (long) a[i] + maxDifference;
            while (bLo < b.length && b[bLo] < a[i]) bLo++;
            while (bHi < b.length && b[bHi] <= hi) bHi++;
            while (cLo < c.length && c[cLo] < a[i]) cLo++;
            while (cHi < c.length && c[cHi] <= hi) cHi++;
            for (int j = bLo; j < bHi; j++) {
                for (int k = cLo; k < cHi; k++) {
                    triples.add(new int[] { i, j, k });
                }
            }
        }

        // B anchors the minimum: A must be strictly greater, C may equal it.
        int aLo = 0, aHi = 0;
        cLo = 0;
        cHi = 0;
        for (int j = 0; j < b.length; j++) {
            long hi = (long) b[j] + maxDifference;
            while (aLo < a.length && a[aLo] <= b[j]) aLo++;
            while (aHi < a.length && a[aHi] <= hi) aHi++;
            while (cLo < c.length && c[cLo] < b[j]) cLo++;
            while (cHi < c.length && c[cHi] <= hi) cHi++;
            for (int i = aLo; i < aHi; i++) {
                for (int k = cLo; k < cHi; k++) {
                    triples.add(new int[] { i, j, k });
                }
            }
        }

        // C anchors the minimum: both A and B must be strictly greater.
        aLo = 0;
        aHi = 0;
        bLo = 0;
        bHi = 0;
        for (int k = 0; k < c.length; k++) {
            long hi = (long) c[k] + maxDifference;
            while (aLo < a.length && a[aLo] <= c[k]) aLo++;
            while (aHi < a.length && a[aHi] <= hi) aHi++;
            while (bLo < b.length && b[bLo] <= c[k]) bLo++;
            while (bHi < b.length && b[bHi] <= hi) bHi++;
            for (int i = aLo; i < aHi; i++) {
                for (int j = bLo; j < bHi; j++) {
                    triples.add(new int[] { i, j, k });
                }
            }
        }

        return triples;
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
