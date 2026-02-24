
import java.lang.reflect.Array;class Solution {
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

        int i = 0; 
        int j = 0;

        int cur = 0;
        int res = 0;

        while(i < n)
        {
            if(start[i] < end[j])
            {
                cur++;
                i++;
            }
            else
            {
                cur--;
                j++;
            }

            res = Math.max(cur, res);
        }

        return res;
    }
}