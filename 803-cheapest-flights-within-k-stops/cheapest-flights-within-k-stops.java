class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        List<List<int[]>> graph = new ArrayList<>();

        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int[] flight: flights)
        {
            graph.get(flight[0]).add(new int[]{flight[1], flight[2]});
        }

        //each graph node list has int[] of (dst, price)

        Deque<int[]> q = new ArrayDeque<>();

        q.offer(new int[]{0, src, 0}); //stops, node, totalpricesofar

        //traverse through levels of stops like a bfs
        int[] dist = new int[n];

        Arrays.fill(dist, (int)1e9);

        while(!q.isEmpty())
        {
            int[] current = q.poll();
            int curStops = current[0];
            int curCity = current[1];
            int curPrice = current[2];

            if(curStops > k)
            {
                break;
            }

            for(int[] nei: graph.get(curCity))
            {
                int neiCity = nei[0];
                int neiPrice = nei[1];

                if(dist[neiCity] > neiPrice + curPrice && curStops <= k)
                {
                    dist[neiCity] = neiPrice + curPrice;
                    q.offer(new int[]{curStops + 1, neiCity, dist[neiCity]});
                }
            }
        }

        if(dist[dst] == (int)1e9)
        {
            return -1;
        }
        else
        {
            return dist[dst];
        }


    }
}