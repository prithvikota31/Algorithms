class Solution {
    public int[] minInterval(int[][] intervals, int[] queries) {
        int m = intervals.length;
        int n = queries.length;

        //sort intervals by start time
        //for query to be in start <= query and query <= end

        Arrays.sort(intervals, (a, b) -> (Integer.compare(a[0], b[0])));

        //now sort queries in increasing order saving the index
        //value, index
        List<int[]> queryWithIndex = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            queryWithIndex.add(new int[]{queries[i], i});
        }
        Collections.sort(queryWithIndex, (a, b) -> Integer.compare(a[0], b[0]));

        //each value is length, end
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        int[] ans = new int[n];
        Arrays.fill(ans, -1);
        int ind = 0;

        for(int[] query: queryWithIndex)
        {
            int orgIndex = query[1];
            int orgQuery = query[0];

            while(ind < m && intervals[ind][0] <= orgQuery) //see start <= q
            {
                minHeap.offer(new int[]{(intervals[ind][1] - intervals[ind][0] + 1), intervals[ind][1]});
                ind++;
            }
            //now heap has all intervals with start <= q

            //next if end < q, remove
            while(!minHeap.isEmpty() && minHeap.peek()[1] < orgQuery)
            {
                minHeap.poll();
            }

            if(!minHeap.isEmpty())
            {
                ans[orgIndex] = minHeap.peek()[0];
            }
        }

        return ans;
    }
}