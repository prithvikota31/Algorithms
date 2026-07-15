/*
 * ============================================================================
 * Problem 14 (Google L4 prep) — Longest Increasing Subsequence with a Bounded
 *                               Adjacent Difference
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given an integer array `nums`, find the LENGTH of the longest SUBSEQUENCE
 * (indices strictly increasing, elements need NOT be contiguous) such that every
 * consecutive pair in the subsequence increases by EXACTLY 1:
 *      nums[k+1] == nums[k] + 1.
 *
 *   Input : int[] nums
 *   Output: length of the longest such subsequence (>= 1 for a non-empty array).
 *
 * FOLLOW-UPS THIS HANDLES
 * -----------------------
 *   (A) Difference AT MOST D: relax the rule to  1 <= nums[k+1] - nums[k] <= D.
 *   (B) Reconstruct the actual subsequence (the path / indices), not just length.
 *
 * EXAMPLES
 * --------
 *   nums = [1,2,3,5,4,5,6]                 -> 6   (1,2,3,4,5,6 in index order)
 *   nums = [10,9,8]                        -> 1   (no increase possible)
 *   nums = [1,3,5,7],  D = 2  (follow-up)  -> 4   (1,3,5,7; each gap 2 <= D)
 *
 * ----------------------------------------------------------------------------
 * INTUITION
 * ----------------------------------------------------------------------------
 * Classic subsequence DP: dp[i] = length of the best chain ENDING at index i.
 *   dp[i] = 1 + max{ dp[j] : j < i and nums[j] is an allowed predecessor }.
 *
 * EXACT DIFF 1  (base):
 *   The only allowed predecessor value is nums[i]-1. If we scan left->right and
 *   keep  best[v] = longest chain ending with value v seen so far, then
 *       best[nums[i]] = best[nums[i]-1] + 1.
 *   Because we scan in index order, best[nums[i]-1] already only reflects
 *   earlier indices, so index order is respected for free.  ->  O(n).
 *
 * DIFF AT MOST D  (follow-up A):
 *   Allowed predecessor VALUES form a RANGE [nums[i]-D, nums[i]-1]. We need the
 *   max dp over that value range among earlier indices. Keep a max-segment-tree
 *   keyed by (compressed) value, storing the best (len, endIndex). For each i:
 *     query the range -> extend -> point-update at value nums[i].  ->  O(n log n).
 *
 * PATH RECONSTRUCTION (follow-up B):
 *   Store a parent pointer for each index (the predecessor index that produced
 *   dp[i]); walk parents back from the global best endpoint and reverse.
 *
 * COMPLEXITY
 *   Exact diff 1 : Time O(n)        Space O(n)
 *   Diff <= D    : Time O(n log n)  Space O(n)   (n = nums.length)
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class LongestSubseqAdjacentDiff {

    // ------------------------------------------------------------------
    // BASE: longest subsequence with adjacent difference EXACTLY 1 (length).
    // ------------------------------------------------------------------
    public int lengthDiffOne(int[] nums) {
        Map<Integer, Integer> best = new HashMap<>();   // value -> best chain length ending at value
        int ans = 0;
        for (int v : nums) {
            int len = best.getOrDefault(v - 1, 0) + 1;
            // keep the max in case value v repeats
            best.put(v, Math.max(best.getOrDefault(v, 0), len));
            ans = Math.max(ans, best.get(v));
        }
        return ans;
    }

    // ------------------------------------------------------------------
    // BASE + PATH: reconstruct the actual subsequence (values), diff EXACTLY 1.
    // ------------------------------------------------------------------
    public List<Integer> pathDiffOne(int[] nums) {
        int n = nums.length;
        int[] parent = new int[n];                       // predecessor index, or -1
        Arrays.fill(parent, -1);
        Map<Integer, Integer> bestLen = new HashMap<>(); // value -> best chain length
        Map<Integer, Integer> bestEnd = new HashMap<>(); // value -> index where that best chain ends

        int globalLen = 0, globalEnd = -1;
        for (int i = 0; i < n; i++) {
            int v = nums[i];
            int len = 1;
            if (bestLen.getOrDefault(v - 1, 0) > 0) {
                len = bestLen.get(v - 1) + 1;
                parent[i] = bestEnd.get(v - 1);
            }
            if (len > bestLen.getOrDefault(v, 0)) {
                bestLen.put(v, len);
                bestEnd.put(v, i);
            }
            if (len > globalLen) {
                globalLen = len;
                globalEnd = i;
            }
        }
        return rebuild(nums, parent, globalEnd);
    }

    // ------------------------------------------------------------------
    // FOLLOW-UP A + B: longest subsequence with 1 <= adjacent diff <= D,
    //                  returning the reconstructed subsequence (values).
    // ------------------------------------------------------------------
    public List<Integer> pathDiffAtMostD(int[] nums, int D) {
        int n = nums.length;
        if (n == 0) return new ArrayList<>();

        // Coordinate-compress the distinct values so the segment tree is small.
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int[] sv = dedup(sorted);                         // sorted distinct values

        SegTree seg = new SegTree(sv.length);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        int globalLen = 0, globalEnd = -1;
        for (int i = 0; i < n; i++) {
            int v = nums[i];
            // predecessor values allowed: [v - D, v - 1]
            int lo = lowerBound(sv, v - D);               // first idx with sv[idx] >= v-D
            int hi = upperBound(sv, v - 1) - 1;           // last  idx with sv[idx] <= v-1

            int len = 1;
            if (lo <= hi) {
                long packed = seg.query(lo, hi);          // best (len, endIndex) in range
                int bestLen = (int) (packed >> 32);
                int bestIdx = (int) packed;
                if (bestLen > 0) {
                    len = bestLen + 1;
                    parent[i] = bestIdx;
                }
            }

            int pos = lowerBound(sv, v);                  // exact position of value v
            seg.update(pos, len, i);

            if (len > globalLen) {
                globalLen = len;
                globalEnd = i;
            }
        }
        return rebuild(nums, parent, globalEnd);
    }

    // ------------------------------------------------------------------
    // Helpers.
    // ------------------------------------------------------------------
    private List<Integer> rebuild(int[] nums, int[] parent, int end) {
        LinkedList<Integer> path = new LinkedList<>();
        for (int i = end; i != -1; i = parent[i]) {
            path.addFirst(nums[i]);
        }
        return path;
    }

    private int[] dedup(int[] sorted) {
        int m = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) sorted[m++] = sorted[i];
        }
        return Arrays.copyOf(sorted, m);
    }

    // first index with a[idx] >= key
    private int lowerBound(int[] a, int key) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < key) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    // first index with a[idx] > key
    private int upperBound(int[] a, int key) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] <= key) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    // Max segment tree over compressed values; each node stores the best
    // (length, endIndex) packed as a long: (len << 32) | (index & 0xffffffffL).
    private static final class SegTree {
        final int size;
        final long[] tree;

        SegTree(int n) {
            size = Math.max(1, n);
            tree = new long[2 * size];       // all zero => len 0
        }

        void update(int pos, int len, int idx) {
            long packed = ((long) len << 32) | (idx & 0xffffffffL);
            int i = pos + size;
            if (packed > tree[i]) {          // higher len wins (index is tie-breaker in low bits)
                tree[i] = packed;
                for (i >>= 1; i >= 1; i >>= 1) {
                    long merged = Math.max(tree[2 * i], tree[2 * i + 1]);
                    if (tree[i] == merged) break;
                    tree[i] = merged;
                }
            }
        }

        // max over [l, r] inclusive
        long query(int l, int r) {
            long res = 0;
            for (l += size, r += size + 1; l < r; l >>= 1, r >>= 1) {
                if ((l & 1) == 1) res = Math.max(res, tree[l++]);
                if ((r & 1) == 1) res = Math.max(res, tree[--r]);
            }
            return res;
        }
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        LongestSubseqAdjacentDiff sol = new LongestSubseqAdjacentDiff();

        int[] a = {1, 2, 3, 5, 4, 5, 6};
        System.out.println(sol.lengthDiffOne(a));        // 6
        System.out.println(sol.pathDiffOne(a));          // [1, 2, 3, 4, 5, 6]

        int[] b = {10, 9, 8};
        System.out.println(sol.lengthDiffOne(b));        // 1
        System.out.println(sol.pathDiffOne(b));          // [10] (any single element)

        int[] c = {1, 3, 5, 7};
        System.out.println(sol.pathDiffAtMostD(c, 2));   // [1, 3, 5, 7]
        System.out.println(sol.pathDiffAtMostD(a, 1));   // [1, 2, 3, 4, 5, 6]
    }
}
