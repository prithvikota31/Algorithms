class Solution {
    public int networkDelayTime(int[][] times, int n, int k) {
        List<List<int[]>> graph = new ArrayList<>();
        //times[i] = (u , v, w)
        for(int i = 0; i <= n; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < times.length; i++)
        {
            graph.get(times[i][0]).add(new int[]{times[i][1], times[i][2]});
        }

        int[] dist = new int[n + 1];

        Arrays.fill(dist, (int)(1e9));
        dist[k] = 0;
        PriorityQueue<Pair> pq = new PriorityQueue<>((a, b) -> a.distance - b.distance);
        pq.offer(new Pair(k, 0));

        while(!pq.isEmpty())
        {
            Pair curPair = pq.poll();
            int curNode = curPair.node;
            int curDist = curPair.distance;

            for(int i = 0; i < graph.get(curNode).size(); i++)
            {
                int neiNode = graph.get(curNode).get(i)[0];
                int neiDist = graph.get(curNode).get(i)[1];

                if(dist[neiNode] > neiDist + curDist)
                {
                    dist[neiNode] = neiDist + curDist;
                    pq.offer(new Pair(neiNode, dist[neiNode]));
                }
            }
        }

        int ans = Integer.MIN_VALUE;
        for(int i = 1; i <= n; i++)
        {
            if(dist[i] == (1e9))    return -1;
            ans = Math.max(ans, dist[i]);
        }

        return ans;
    }
}

class Pair{
    int node;
    int distance;

    public Pair(int node, int distance)
    {
        this.node = node;
        this.distance = distance;
    }
}