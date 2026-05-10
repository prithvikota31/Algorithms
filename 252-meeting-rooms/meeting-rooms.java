class Solution {
    public boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        if(intervals.length <= 1)   return true;
        //minimum intervals length = 2
        int[] lastMeeting = intervals[0];

        //check overlap for each meeting with previous one, if yes, return false
        for(int i = 1; i < intervals.length; i++)
        {
            if(lastMeeting[0] < intervals[i][1] && 
                lastMeeting[1] > intervals[i][0])
            {
                return false;     
            }
            lastMeeting = intervals[i];

        }

        return true;
    }
}