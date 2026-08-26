class Solution {
    public int[] minInterval(int[][] intervals, int[] queries) {
        //first task is to sort intervals by start
        //queries by earliest, along with its indices
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> queryWithIndex = new ArrayList<>();
        for(int i = 0; i < queries.length; i++)
        {
            queryWithIndex.add(new int[]{i, queries[i]}); //{index, query};
        }

        Collections.sort(queryWithIndex, (a, b) -> Integer.compare(a[1], b[1]));


        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0])); //{interval, start end}

        int intervalIndex = 0;
        int[] ans = new int[queries.length];
        Arrays.fill(ans, -1);
        //process query by query
        int q = 0;
        while(q < queries.length)
        {
            int queryIndex = queryWithIndex.get(q)[0];
            int query = queryWithIndex.get(q)[1];

            //start < query
            while(intervalIndex < intervals.length && intervals[intervalIndex][0] <= query)
            {
                minHeap.offer(new int[]{intervals[intervalIndex][1] - intervals[intervalIndex][0] + 1, 
                                    intervals[intervalIndex][0] ,intervals[intervalIndex][1]});
                intervalIndex++;
            }

            //end < query (remove) (even equal is captured by query)
            while(!minHeap.isEmpty() && minHeap.peek()[2] < query)
            {
                minHeap.poll();
            }
            if(!minHeap.isEmpty())
            {
                ans[queryIndex] = minHeap.peek()[0];
            }

            q++;
        }

        return ans;
    }
}