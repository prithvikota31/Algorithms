import java.util.Arrays;

class Solution {
    public int minMeetingRooms(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }

        int n = intervals.length;

        int[] start = new int[n];
        int[] end = new int[n];

        // Separate start and end times
        for (int i = 0; i < n; i++) {
            start[i] = intervals[i][0];
            end[i] = intervals[i][1];
        }

        Arrays.sort(start);
        Arrays.sort(end);

        int rooms = 0;
        int endPtr = 0;

        for (int i = 0; i < n; i++) {

            // If next meeting starts before earliest meeting ends
            if (start[i] < end[endPtr]) {
                rooms++;          // need new room
            } else {
                endPtr++;         // reuse a freed room
            }
        }

        return rooms;
    }
}