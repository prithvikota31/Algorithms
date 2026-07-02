class Solution {
    public int minCostConnectPoints(int[][] points) {
        int v = points.length;

        PriorityQueue<Pair> pq = new PriorityQueue<>((a, b) -> (a.distance - b.distance));

        int[] visited = new int[v];
        int minSum = 0;
        int nodesUsed = 0;
        pq.offer(new Pair(0, 0));

        while(nodesUsed < v)
        {
            Pair cur = pq.poll();
            int cNode = cur.node;
            int cDistance = cur.distance;

            if(visited[cNode] == 1) continue; //already visited with less wieght

            visited[cNode] = 1;
            minSum += cDistance;
            nodesUsed++;

            
            for(int i = 0; i < v; i++)
            {
                if(visited[i] == 0)
                {
                    //points (xi, yi)
                    int neighDistance = Math.abs(points[cNode][0] - points[i][0]) 
                                                + Math.abs(points[cNode][1] - points[i][1]);
                    pq.offer(new Pair(i, neighDistance));
                }
            }
        }

        return minSum;
    }


    class Pair{
        int node;
        int distance;

        public Pair(int n, int d)
        {
            node = n;
            distance = d;
        }
    }
}