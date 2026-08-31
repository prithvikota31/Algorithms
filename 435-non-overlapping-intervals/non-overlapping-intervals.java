class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        int l = intervals.length;

        Arrays.sort(intervals, (m1, m2) -> Integer.compare(m1[1], m2[1]));

        int ans = 0;
        int preIntervalEnd = intervals[0][1];

        for(int i = 1; i < l; i++)
        {
            if(intervals[i][0] >= preIntervalEnd)
            {
                preIntervalEnd = intervals[i][1];

            }
            else
            {
                ans++;
            }
        }

        return ans;  
    }
}