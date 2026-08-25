class Solution {
    public int minMeetingRooms(int[][] intervals) {
        int n = intervals.length;

        int[] start = new int[n];
        int[] end = new int[n];
        for(int i = 0; i < n; i++)
        {
            start[i] = intervals[i][0];
            end[i] = intervals[i][1];
        }

        Arrays.sort(start);
        Arrays.sort(end);
        int usingRooms = 0;
        int s = 0;
        int e = 0;
        int max = 0;
        while(s < n && e < n)
        {
            if(start[s] < end[e])
            {
                usingRooms++;
                s++;
            }
            else
            {
                usingRooms--;
                e++;
            }
            max = Math.max(max, usingRooms);

        }
        return max;
    }
}