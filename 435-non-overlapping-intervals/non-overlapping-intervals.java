class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        int n = intervals.length;
        if(n <= 1)  return 0;
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
        //sort based on end times, so we have more time left to fill other intervals
        int[] last = intervals[0];
        int erase = 0;
        for(int i = 1; i < n; i++)
        {
            //check overlap
            if(last[0] < intervals[i][1] && intervals[i][0] < last[1])
            {
                erase++;
            }
            else
            {
                last = intervals[i];
            }
        }
        return erase;
    }
}