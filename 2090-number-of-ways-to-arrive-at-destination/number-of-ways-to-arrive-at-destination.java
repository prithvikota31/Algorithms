import java.util.*;

class Solution {
    public int countPaths(int n, int[][] roads) {

        int MOD = 1_000_000_007;

        List<List<int[]>> graph = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Undirected graph
        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            int time = road[2];

            graph.get(u).add(new int[]{v, time});
            graph.get(v).add(new int[]{u, time});
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);

        int[] ways = new int[n];

        // {distance, node}
        PriorityQueue<long[]> minHeap =
                new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));

        dist[0] = 0;
        ways[0] = 1;

        minHeap.offer(new long[]{0, 0});

        while (!minHeap.isEmpty()) {

            long[] cur = minHeap.poll();

            long cDist = cur[0];
            int cNode = (int) cur[1];

            // stale entry
            if (cDist > dist[cNode]) {
                continue;
            }

            for (int[] nei : graph.get(cNode)) {

                int neiNode = nei[0];
                int neiTime = nei[1];

                long newDist = cDist + neiTime;

                // Found a strictly shorter path
                if (newDist < dist[neiNode]) {

                    dist[neiNode] = newDist;

                    // All shortest ways to cNode now lead to neiNode
                    ways[neiNode] = ways[cNode];

                    minHeap.offer(
                        new long[]{newDist, neiNode}
                    );
                }

                // Found another path with same shortest distance
                else if (newDist == dist[neiNode]) {

                    ways[neiNode] =
                            (ways[neiNode] + ways[cNode]) % MOD;
                }
            }
        }

        return ways[n - 1];
    }
}