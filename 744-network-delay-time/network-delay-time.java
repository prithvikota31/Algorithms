class Solution {
    public int networkDelayTime(int[][] times, int n, int k) {
        // Build adjacency list.
        // graph[u] contains all outgoing edges from u: {v, weight}
        List<List<int[]>> graph = new ArrayList<>();

        // Nodes are 1-indexed, so we create n + 1 lists.
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        // times[i] = {u, v, w}
        // Meaning: signal goes from u -> v in w time.
        for (int[] time : times) {
            int u = time[0];
            int v = time[1];
            int w = time[2];

            graph.get(u).add(new int[]{v, w});
        }

        // dist[i] = shortest time needed to reach node i from source k.
        int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n + 1];
        Arrays.fill(dist, INF);

        // Source node takes 0 time to reach itself.
        dist[k] = 0;

        // Min-heap ordered by current shortest known distance.
        PriorityQueue<Pair> pq = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.distance, b.distance)
        );

        pq.offer(new Pair(k, 0));

        while (!pq.isEmpty()) {
            Pair curPair = pq.poll();

            int curNode = curPair.node;
            int curDist = curPair.distance;

            // Important:
            // Same node can enter heap multiple times.
            // If this is an older/larger distance, ignore it.
            if (curDist > dist[curNode]) {
                continue;
            }

            // Try improving distances of all neighbors.
            for (int[] edge : graph.get(curNode)) {
                int neiNode = edge[0];
                int edgeWeight = edge[1];

                // Relaxation step:
                // If going through curNode gives a shorter path to neiNode, update it.
                if (dist[neiNode] > curDist + edgeWeight) {
                    dist[neiNode] = curDist + edgeWeight;
                    pq.offer(new Pair(neiNode, dist[neiNode]));
                }
            }
        }

        // Network delay = time when the last node receives the signal.
        // So take maximum among all shortest distances.
        int ans = 0;

        for (int i = 1; i <= n; i++) {
            // Some node was never reached.
            if (dist[i] == INF) {
                return -1;
            }

            ans = Math.max(ans, dist[i]);
        }

        return ans;
    }
}

class Pair {
    int node;
    int distance;

    public Pair(int node, int distance) {
        this.node = node;
        this.distance = distance;
    }
}