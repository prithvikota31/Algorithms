class Solution {
    public boolean canAttendMeetings(int[][] intervals) {
        if(intervals.length == 0)   return true;
        //sort based on end times
        Arrays.sort(intervals, (a, b) -> (a[1] - b[1]));
        int lastEndTime = intervals[0][1]; //first meeting happens for sure
        for(int i = 1; i < intervals.length; i++)
        {
            if(intervals[i][0] < lastEndTime)   return false;
            else
            {
                lastEndTime = intervals[i][1];
            }
        } 
        return true;
    }
}