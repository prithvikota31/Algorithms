class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> (a[1] - b[1])); //sort based on end times

        // 1, 3
        // 2, 4
        int[] currentInterval = intervals[0];
        int count = 0;
        for(int i = 1; i < intervals.length; i++)
        {
            if(currentInterval[1] <= intervals[i][0])
            {
                currentInterval = intervals[i];
            }
            else
            {
                count++;
            }
        }
        return count;
    }
}