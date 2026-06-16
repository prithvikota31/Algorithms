class Solution {
    public int minMeetingRooms(int[][] intervals) {
        if (intervals.length == 0) return 0;

        // Process meetings in the order they start.
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // Min-heap stores end times of currently active meetings.
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int ans = 0;

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // Release all rooms whose meetings already ended.
            while (!minHeap.isEmpty() && minHeap.peek() <= start) {
                minHeap.poll();
            }

            // Current meeting occupies one room.
            minHeap.offer(end);

            ans = Math.max(ans, minHeap.size());
        }

        return ans;
    }
}