class Solution {
    public int minMeetingRooms(int[][] intervals) {
        int n = intervals.length;
        if (n == 0) return 0;

        Arrays.sort(intervals,
            (a, b) -> Integer.compare(a[0], b[0]));

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        int maxRooms = 0;

        for (int i = 0; i < n; i++) {
            int start = intervals[i][0];
            int end = intervals[i][1];

            while (!minHeap.isEmpty() && start >= minHeap.peek()) {
                minHeap.poll();
            }

            minHeap.offer(end);
            maxRooms = Math.max(maxRooms, minHeap.size());
        }

        return maxRooms;
    }
}