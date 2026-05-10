class Solution {
    public int minMeetingRooms(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> (a[0] - b[0])); //sorted by start times

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for(int[] interval: intervals)
        {
            if(!minHeap.isEmpty() && interval[0] >= minHeap.peek())
            {
                minHeap.poll();
            }

            minHeap.offer(interval[1]);
        }

        return minHeap.size();
    }
}