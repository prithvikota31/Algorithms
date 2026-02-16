import java.util.*;

class Pair {
    int node;
    int distance;

    Pair(int distance, int node) {
        this.distance = distance;
        this.node = node;
    }
}

class Solution {

    public int minCostConnectPoints(int[][] points) {

        int V = points.length;

        PriorityQueue<Pair> pq =
            new PriorityQueue<>((x, y) -> x.distance - y.distance);

        int[] vis = new int[V];

        // start from node 0
        pq.add(new Pair(0, 0));

        int sum = 0;

        while (!pq.isEmpty()) {

            Pair cur = pq.poll();

            int node = cur.node;
            int wt = cur.distance;

            if (vis[node] == 1)
                continue;

            vis[node] = 1;

            sum += wt;

            // add all neighbors
            for (int adjNode = 0; adjNode < V; adjNode++) {

                if (vis[adjNode] == 0) {

                    int dist =
                        Math.abs(points[node][0] - points[adjNode][0]) +
                        Math.abs(points[node][1] - points[adjNode][1]);

                    pq.add(new Pair(dist, adjNode));
                }
            }
        }

        return sum;
    }
}
