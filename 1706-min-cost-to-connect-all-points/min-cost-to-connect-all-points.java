class Solution {
    public int minCostConnectPoints(int[][] points) {
        int n = points.length;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));

        //start from point zero
        //int[] contains point index, edgeweight
        pq.offer(new int[]{0, 0});

        boolean[] visited = new boolean[n];
        int min = 0;

        while(!pq.isEmpty())
        {
            int[] cur = pq.poll();
            int curIndex = cur[0];
            int curEdgeWeight = cur[1];

            if(visited[curIndex])
            {
                continue;
            }
            visited[curIndex] = true;
            min += curEdgeWeight;
            for(int i = 0; i < n; i++)
            {
                if(!visited[i])
                {
                    int nextEdgeWt = Math.abs(points[curIndex][0] - points[i][0]) 
                                            + Math.abs(points[curIndex][1] - points[i][1]);
                    pq.offer(new int[]{i, nextEdgeWt});
                }
            }
        }

        return min;
    }
}