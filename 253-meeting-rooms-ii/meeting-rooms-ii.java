class Solution {
    public int minMeetingRooms(int[][] intervals) {
        int[] start = new int[intervals.length];
        int[] end = new int[intervals.length];

        for(int i = 0; i < intervals.length; i++)
        {
            start[i] = intervals[i][0];
            end[i] = intervals[i][1];
        }

        Arrays.sort(start);
        Arrays.sort(end);

        int rooms = 0;
        int maxRooms = 0;
        int i = 0; //tracking start
        int j = 0; //tracking end
        while(i < intervals.length && j < intervals.length)
        {
            if(start[i] < end[j])
            {
                rooms++;
                i++;
            }
            else
            {
                rooms--;
                j++;
            }

            maxRooms = Math.max(rooms, maxRooms);

        }

        return maxRooms;
    }
}