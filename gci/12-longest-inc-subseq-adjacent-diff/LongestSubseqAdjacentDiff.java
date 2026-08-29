/*
 * ============================================================================
 * Problem 12 (Google L4 prep) — Longest Increasing Subsequence with a Bounded
 *                               Adjacent Difference   [O(n) base + O(n^2) DP]
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
 * Classic subsequence DP.  Define
 *      dp[i] = length of the longest valid chain ENDING at index i.
 * A chain ending at i either starts fresh (length 1) or extends some earlier
 * chain ending at an ALLOWED predecessor j (j < i and the step is legal):
 *      dp[i] = 1 + max{ dp[j] : j < i and step(nums[j], nums[i]) is allowed }.
 * The answer is max(dp).  We simply try every j < i for each i -> O(n^2).
 *
 *   - EXACT DIFF 1  : step allowed when nums[i] - nums[j] == 1.
 *   - DIFF AT MOST D: step allowed when 1 <= nums[i] - nums[j] <= D.
 *
 * PATH RECONSTRUCTION (follow-up B):
 *   parent[i] = the j that gave dp[i] its value (or -1 if it started fresh).
 *   Track the index with the global max dp; walk parents back and reverse.
 *
 * COMPLEXITY
 *   Base diff-1, OPTIMAL: lengthDiffOneOptimal (length) and
 *     indicesDiffOneOptimalMap (path indices) -> O(n) time / O(n) space
 *     (value-keyed DP; the only legal predecessor of value x is x - 1, so no
 *      inner scan is needed).
 *   diff<=D:
 *     lengthDiffAtMostDOptimal -> O(n log M) time / O(M) space (M = max value),
 *       segment tree keyed by VALUE: range-max over [value-D, value-1] +
 *       point-update at `value`. Assumes values >= 1 (LeetCode 2407).
 *     Path reconstruction (pathDiffAtMostD) stays O(n^2) time / O(n) space.
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class LongestSubseqAdjacentDiff {

    // ------------------------------------------------------------------
    // BASE: longest subsequence with adjacent difference EXACTLY 1 (length).
    // ------------------------------------------------------------------
    public int lengthDiffOne(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        int answer = 1;
        // dp[i] = longest valid subsequence ending at index i
        for(int i = 1; i < n; i++)
        {
            for(int j = 0; j < i; j++)
            {
                if((long) nums[i] - nums[j] == 1 &&  1 + dp[j] > dp[i])
                {
                    dp[i] = 1 + dp[j];
                }
            }
            answer = Math.max(answer, dp[i]);
        }

        return answer;
    }

    // ------------------------------------------------------------------
    // BASE, OPTIMAL O(n): longest subsequence with adjacent difference
    // EXACTLY 1. The only legal predecessor of a value x is x - 1, so instead
    // of scanning earlier indices we key the DP by VALUE:
    //      bestLen[x] = bestLen[x - 1] + 1
    // long keys avoid overflow when x == Integer.MIN_VALUE (x - 1 underflow).
    // ------------------------------------------------------------------
    public int lengthDiffOneOptimal(int[] nums) {
        if(nums == null || nums.length == 0)
        {
            return 0;
        }
        //map stores value -> best length so far
        Map<Long, Integer> bestLength = new HashMap<>();

        int max = 0;
        for(int num: nums)
        {
            long value = num;
            //best possible so far
            int bestPossible = bestLength.getOrDefault(value - 1, 0);

            int maxCurrentNumLength = Math.max(bestLength.getOrDefault(value, 0), bestPossible + 1);

            bestLength.put(value, maxCurrentNumLength);
            max = Math.max(max, maxCurrentNumLength);
        }
        return max;
    }

    // ------------------------------------------------------------------
    // BASE + PATH, OPTIMAL O(n): reconstruct the INDICES of the longest
    // subsequence with adjacent difference EXACTLY 1. long value-keys avoid
    // the Integer.MIN_VALUE - 1 underflow.
    // ------------------------------------------------------------------

    public List<Integer> indicesDiffOneOptimalV2(int[] nums) {
        if(nums == null || nums.length == 0)
        {
            return new ArrayList<>();
        }
        int n = nums.length;

        Map<Long, Integer> bestValueIndex = new HashMap<>(); //value -> index

        int[] parent = new int[n];
        int[] length = new int[n]; //gives best length ending at i
        Arrays.fill(parent, -1);

        for(int i = 0; i < n; i++)
        {
            long currentValue = nums[i];

            Integer previousIndex = bestValueIndex.get(currentValue - 1);
            if(previousIndex == null) //nothing exists before this value
            {
                length[i] = 1;
            }
            else
            {
                length[i] = length[previousIndex] + 1;
                parent[i] = previousIndex;
            }

            //check if currentValue have a previous best
            Integer currentValueBestIndex = bestValueIndex.get(currentValue);

            if(currentValueBestIndex == null 
                || length[i] > length[currentValueBestIndex])
            {
                bestValueIndex.put(currentValue, i);
            }
        }
        int bestendingIndex = -1;
        //for best index, loop through length to get it
        for(int i = 0; i < n; i++)
        {
            if(bestendingIndex == -1 || length[i] > length[bestendingIndex])
            {
                bestendingIndex = i;
            }
        }

        //now get the result
        List<Integer> path = new ArrayList<>();

        //start with bestendingIndex
        int index = bestendingIndex;
        while(index != -1)
        {
            path.add(index);
            index = parent[index];
        }

        Collections.reverse(path);
        return path;
    }

    public List<Integer> indicesDiffOneOptimalMap(int[] nums)
    {
        if(nums == null || nums.length == 0)
        {
            return new ArrayList<>();
        }
        int n = nums.length;
        Map<Long, Integer> bestValueIndexMap = new HashMap<>();
        //val -> (i)index (this val has best max at index i)

        int[] parent = new int[n];
        int[] length = new int[n];

        Arrays.fill(parent, -1);
        Arrays.fill(length, 1);

        for(int i = 0; i < n; i++)
        {
            long value = nums[i];

            if(!bestValueIndexMap.containsKey(value - 1))
            {
                length[i] = 1;
            }
            else //value - 1 best is present
            {
                int bestIndexValMinusOne = bestValueIndexMap.get(value - 1);
                length[i] = length[bestIndexValMinusOne] + 1;
                parent[i] = bestIndexValMinusOne;
            }

            //check current value's best ending index
            Integer bestIndexCurVal = bestValueIndexMap.get(value);

            if(bestIndexCurVal == null || length[i] > length[bestIndexCurVal])
            {
                bestValueIndexMap.put(value, i);
            }
        }

        // now find bestIndex with maxValue
        int maxIndex = 0;
        int max = 1;

        for(int i = 0; i < n; i++)
        {
            if(length[i] > max)
            {
                maxIndex = i;
                max = length[i];
            }
        }

        List<Integer> result = new ArrayList<>();
        int cur = maxIndex;
        while(parent[cur] != -1)
        {
            result.add(cur);
            cur = parent[cur];
        }
        result.add(cur);
        Collections.reverse(result);
        return result;
    }

    public List<Integer> indicesDiffOneOptimalDP(int[] nums)
    {
        if(nums == null || nums.length == 0)
        {
            return new ArrayList<>();
        }
        int n = nums.length;

        int[] parent = new int[n];
        int[] dp = new int[n];

        Arrays.fill(parent, -1);
        Arrays.fill(dp, 1);
        
        for(int i = 1; i < n; i++)
        {
            for(int j = 0; j < i; j++)
            {
                if((long) nums[i] - nums[j] == 1 && dp[i] < dp[j] + 1)
                {
                    parent[i] = j;
                    dp[i] = dp[j] + 1;
                } 
            }
        }
        //now we got parent array and dp array
        //find max value in DP
        int maxIndex = 0;
        int maxValue = 1;
        for(int i = 0; i < n; i++)
        {
            if(dp[i] > maxValue)
            {
                maxIndex = i;
                maxValue = dp[i];
            }
        }

        List<Integer> result = new ArrayList<>();
        int cur = maxIndex;
        while(parent[cur] != -1)
        {
            result.add(cur);
            cur = parent[cur];
        }
        result.add(cur);
        Collections.reverse(result);
        return result;
    }
    // ------------------------------------------------------------------
    // BASE + PATH: reconstruct the actual subsequence (values), diff EXACTLY 1.
    // ------------------------------------------------------------------
    public List<Integer> pathDiffOne(int[] nums) {
        return solve(nums, 1);              // D = 1 means the only step is +1
    }

    // ------------------------------------------------------------------
    // FOLLOW-UP A + B: longest subsequence with 1 <= adjacent diff <= D,
    //                  returning the reconstructed subsequence (values).
    // ------------------------------------------------------------------
    public List<Integer> pathDiffAtMostD(int[] nums, int D) {
        return solve(nums, D);
    }



    // ------------------------------------------------------------------
    // Shared O(n^2) DP + path reconstruction.
    // A step j -> i is allowed when  1 <= nums[i] - nums[j] <= D.
    // (D = 1 gives the "exactly 1" base problem.)
    // ------------------------------------------------------------------
    private List<Integer> solve(int[] nums, int D) {
        if (nums == null || nums.length == 0) return new ArrayList<>();

        int n = nums.length;

        int[] dp = new int[n];              // dp[i] = best chain length ending at i
        int[] parent = new int[n];          // predecessor index for dp[i], or -1
        Arrays.fill(parent, -1);

        int globalLen = 0, globalEnd = -1;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                long diff = (long) nums[i] - nums[j];
                if (diff >= 1 && diff <= D && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > globalLen) {
                globalLen = dp[i];
                globalEnd = i;
            }
        }

        List<Integer> result = new ArrayList<>();
        int cur = globalEnd;
        while(parent[cur] != -1)
        {
            result.add(nums[cur]);
            cur = parent[cur];
        }
        result.add(nums[cur]);
        Collections.reverse(result);
        return result;
    }



    // ------------------------------------------------------------------
    // FOLLOW-UP A, OPTIMAL O(n log M): LENGTH of the longest strictly-
    // increasing subsequence with 1 <= adjacent diff <= D  (LeetCode 2407).
    //
    // Value-keyed DP: bestLength[value] = longest chain ending EXACTLY at value.
    // To append `value`, the previous value must lie in [value - D, value - 1]
    // (upper bound value-1 keeps it STRICTLY increasing). A segment tree over
    // the value axis answers that range-max window and the point-update at
    // `value` in O(log M). Assumes values >= 1 (LeetCode: 1 <= nums[i] <= 1e5).
    // ------------------------------------------------------------------
    public int lengthDiffAtMostDOptimal(int[] nums, int D) {
        // Phase 1: edge cases.
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (D <= 0) {
            return 1;               // no adjacent step is legal -> a single element is best
        }

        // Phase 2: size the tree. Positions ARE the numeric values, so size = max value.
        int maxValue = 0;
        for (int value : nums) {
            maxValue = Math.max(maxValue, value);
        }

        SegmentTree tree = new SegmentTree(maxValue);
        int longest = 1;

        // Phase 3: core DP. Process values left-to-right so predecessors are
        // exactly the elements already inserted (original order preserved).
        for (int value : nums) {
            // Legal predecessors live in [value - D, value - 1].
            int left = Math.max(1, value - D);
            int right = value - 1;

            int bestPrevious = tree.query(left, right);
            int current = bestPrevious + 1;

            // Duplicate values: keep the best chain ending exactly at `value`.
            tree.update(value, current);
            longest = Math.max(longest, current);
        }

        return longest;
    }

    // ------------------------------------------------------------------
    // Range-max segment tree over the value axis (1..size). O(log size) per op.
    //   tree[node] = max DP length stored in that node's value range.
    // ------------------------------------------------------------------
    private static class SegmentTree {
        private final int[] tree;
        private final int size;

        SegmentTree(int size) {
            this.size = size;
            this.tree = new int[4 * size];
        }

        // Max DP length stored in values [queryLeft, queryRight] (0 if empty).
        int query(int queryLeft, int queryRight) {
            if (queryLeft > queryRight) {
                return 0;
            }
            return query(1, 1, size, queryLeft, queryRight);
        }

        private int query(int node, int segLeft, int segRight, int qLeft, int qRight) {
            if (qRight < segLeft || segRight < qLeft) {      // no overlap
                return 0;
            }
            if (qLeft <= segLeft && segRight <= qRight) {    // full overlap
                return tree[node];
            }
            int mid = segLeft + (segRight - segLeft) / 2;
            return Math.max(
                    query(node * 2, segLeft, mid, qLeft, qRight),
                    query(node * 2 + 1, mid + 1, segRight, qLeft, qRight));
        }

        // Point update: best length ending exactly at `value`.
        void update(int value, int newLength) {
            update(1, 1, size, value, newLength);
        }

        private void update(int node, int segLeft, int segRight, int value, int newLength) {
            if (segLeft == segRight) {
                tree[node] = Math.max(tree[node], newLength);
                return;
            }
            int mid = segLeft + (segRight - segLeft) / 2;
            if (value <= mid) {
                update(node * 2, segLeft, mid, value, newLength);
            } else {
                update(node * 2 + 1, mid + 1, segRight, value, newLength);
            }
            tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);   // restore range-max
        }
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        LongestSubseqAdjacentDiff sol = new LongestSubseqAdjacentDiff();

        int[] a = {1, 2, 3, 5, 4, 5, 6};
        System.out.println(sol.lengthDiffOne(a));        // 6
        System.out.println(sol.lengthDiffOneOptimal(a)); // 6  (O(n) value-keyed)
        System.out.println(sol.pathDiffOne(a));          // [1, 2, 3, 4, 5, 6]

        int[] b = {10, 9, 8};
        System.out.println(sol.lengthDiffOne(b));        // 1
        System.out.println(sol.pathDiffOne(b));          // [10] (any single element)

        int[] c = {1, 3, 5, 7};
        System.out.println(sol.pathDiffAtMostD(c, 2));   // [1, 3, 5, 7]
        System.out.println(sol.pathDiffAtMostD(a, 1));   // [1, 2, 3, 4, 5, 6]

        int[] d = {2, 3, 1, 4, 3, 5, 6};
        System.out.println(sol.indicesDiffOneOptimalV2(d)); // [0, 1, 3, 5, 6]

        int[] e = {5, 6, 7, 1, 2, 3};                        // two runs of len 3; earliest wins
        System.out.println(sol.indicesDiffOneOptimalV2(e)); // [0, 1, 2]  (values 5,6,7)

        int[] f = {4, 4, 5, 6};                              // duplicate 4; still values 4,5,6
        System.out.println(sol.indicesDiffOneOptimalV2(f)); // [0, 2, 3]  (values 4,5,6)

        int[] g = {4, 2, 1, 4, 3, 4, 5, 8, 15};              // diff<=D, O(n log M) segment tree
        System.out.println(sol.lengthDiffAtMostDOptimal(g, 3)); // 5  (1,3,4,5,8)
    }
}
