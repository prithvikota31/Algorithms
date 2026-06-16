class Solution {
    public int minMeetingRooms(int[][] intervals) {
        if (intervals.length == 0) return 0;

        int n = intervals.length;
        int[] starts = new int[n];
        int[] ends = new int[n];

        // Split start times and end times.
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }

        // Sort both timelines independently.
        Arrays.sort(starts);
        Arrays.sort(ends);

        int rooms = 0;
        int endPtr = 0;
 
        // Process meetings by start time.
        for (int startPtr = 0; startPtr < n; startPtr++) {

            // Earliest meeting ended, so reuse that room.
            if (starts[startPtr] >= ends[endPtr]) {
                endPtr++;
            } 
            // No room free, need a new room.
            else {
                rooms++;
            }
        }

        return rooms;
    }
}