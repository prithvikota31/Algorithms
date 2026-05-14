class Solution {
    public int[] minInterval(int[][] intervals, int[] queries) {
        int n = queries.length;

        //sort intervals by start
        //sort queries based on values but store the index
        Arrays.sort(intervals, (a, b)-> (a[0] - b[0]));

        List<QueryPair> queryPairIndexList = new ArrayList<>();
        for(int i = 0; i < queries.length; i++)
        {
            queryPairIndexList.add(new QueryPair(i, queries[i]));
        }

        Collections.sort(queryPairIndexList, (a, b) -> (a.value - b.value));
        int[] result = new int[n];
        //size = end - start + 1;
        PriorityQueue<IntervalPair> pq = new PriorityQueue<>((a, b) -> (a.size - b.size));

        int ind = 0;
        //for each query sweep from left to right
        for(int i = 0; i < queryPairIndexList.size(); i++)
        {
            QueryPair curQuery = queryPairIndexList.get(i);

            while(ind < intervals.length && intervals[ind][0] <= curQuery.value)
            {
                int tSize = intervals[ind][1] - intervals[ind][0] + 1;
                pq.offer(new IntervalPair(intervals[ind][1], tSize));
                ind++;
            }

            while(!pq.isEmpty() && pq.peek().end < curQuery.value)
            {
                pq.poll();
            }

            if(pq.isEmpty())
            {
                result[curQuery.index] = -1;
            }
            else
            {
                result[curQuery.index] = pq.peek().size;
            }

        }

        return result;
        
    }


}

class IntervalPair{

    int end;
    int size;
    public IntervalPair(int end, int size)
    {
        this.end = end;
        this.size = size;
    }
}

class QueryPair{
    int index;
    int value;
    public QueryPair(int ind, int val)
    {
        index = ind;
        value = val;
    }
}