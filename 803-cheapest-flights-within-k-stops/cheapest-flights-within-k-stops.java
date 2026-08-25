class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        List<List<int[]>> graph = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
        }

        int[] dist = new int[n];
        Arrays.fill(dist, (int)1e9);
        for(int[] flight: flights)
        {
            int u = flight[0];
            int v = flight[1];
            int wt = flight[2];
            graph.get(u).add(new int[]{v, wt});
        }

        dist[src] = 0;
        //k stops = k + 1 flights
        //when we poll k + 1 usage break theloop
        //flightsused, u, wt
        Deque<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{0, src, 0});
        while(!q.isEmpty())
        {
            int[] cur = q.poll();
            int cFlightsUsed = cur[0];
            int cNode = cur[1];
            int cWeight = cur[2];

            if(cFlightsUsed == k + 1)
            {
                break;
            }

            for(int[] nei: graph.get(cNode))
            {
                int neiNode = nei[0];
                int neiWt = nei[1];

                if(cWeight + neiWt < dist[neiNode])
                {
                    dist[neiNode] = cWeight + neiWt;
                    q.offer(new int[]{cFlightsUsed + 1, neiNode, dist[neiNode]});
                }
            }

        }

        if(dist[dst] == (int)1e9)
        {
            return -1;
        }
        return dist[dst];
    }
}