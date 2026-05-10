class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {

        Arrays.sort(intervals, (a, b) -> (a[1] - b[1])); //sort based on end times
        int l = intervals.length;
        int count = 0;
        //think greedy, finish the interval with lowest end, so that it can accomodate space for others

        int lastEnd = intervals[0][1];

        for(int i = 1; i < l; i++)
        {
            //cur is intervals[i]
            if(intervals[i][0] < lastEnd)
            {
                count++;
            }
            else
            {
                lastEnd = intervals[i][1];
            }
        }

        return count;
    }
}