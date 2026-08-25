class Solution {
    public boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
        //sorted based on end times

        // if any adjacent interval overlap return false
        // finally true

        for(int i = 0; i <= intervals.length - 2; i++)
        {
            int[] interval1 = intervals[i];
            int[] interval2 = intervals[i + 1];

            //to overlap start < end 
            if(interval1[0] < interval2[1] && interval2[0] < interval1[1])
            {
                return false;
            }
        }
        return true;
    }
}