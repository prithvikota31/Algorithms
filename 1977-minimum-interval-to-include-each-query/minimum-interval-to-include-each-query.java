class Solution {
    public int[] minInterval(int[][] intervals, int[] queries) {

        int n = queries.length;

        // answer array
        int[] result = new int[n];

        // queries with original index
        int[][] q = new int[n][2];

        for (int i = 0; i < n; i++) {
            q[i][0] = queries[i];
            q[i][1] = i;
        }

        // sort intervals by start
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // sort queries by value
        Arrays.sort(q, (a, b) -> a[0] - b[0]);

        // minHeap -> [intervalSize, intervalEnd]
        PriorityQueue<int[]> minHeap =
            new PriorityQueue<>((a, b) -> a[0] - b[0]);

        int i = 0;

        for (int[] query : q) {

            int queryValue = query[0];
            int originalIndex = query[1];

            // add all intervals whose start <= query
            while (i < intervals.length &&
                   intervals[i][0] <= queryValue) {

                int start = intervals[i][0];
                int end = intervals[i][1];

                int size = end - start + 1;

                minHeap.offer(new int[]{size, end});

                i++;
            }

            // remove intervals that cannot contain query
            while (!minHeap.isEmpty() &&
                   minHeap.peek()[1] < queryValue) {

                minHeap.poll();
            }

            // heap top = smallest valid interval
            if (minHeap.isEmpty()) {
                result[originalIndex] = -1;
            } else {
                result[originalIndex] = minHeap.peek()[0];
            }
        }

        return result;
    }
}