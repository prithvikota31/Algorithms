class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int n = intervals.length;

        List<int[]> ans = new ArrayList<>();

        int index = 0;

        int mergedStart = newInterval[0];
        int mergedEnd = newInterval[1];

        while(index < n && intervals[index][1] < mergedStart)
        {
            ans.add(new int[]{intervals[index][0], intervals[index][1]});
            index++;
        }

        while(index < n && intervals[index][1] >= mergedStart && intervals[index][0] <= mergedEnd)
        {
            mergedStart = Math.min(mergedStart, intervals[index][0]);
            mergedEnd = Math.max(mergedEnd, intervals[index][1]);
            index++;
        }

        ans.add(new int[]{mergedStart, mergedEnd});

        while(index < n)
        {
            ans.add(new int[]{intervals[index][0], intervals[index][1]});
            index++;
        }

        return ans.toArray(new int[0][]);
    }
}