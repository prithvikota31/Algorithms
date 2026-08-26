class Solution {
    public int minMeetingRooms(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int maxRooms = 0;

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // Remove every meeting that has already ended
            while (!minHeap.isEmpty() && minHeap.peek() <= start) {
                minHeap.poll();
            }

            // Current meeting now occupies one room
            minHeap.offer(end);

            maxRooms = Math.max(maxRooms, minHeap.size());
        }

        return maxRooms;
    }
}