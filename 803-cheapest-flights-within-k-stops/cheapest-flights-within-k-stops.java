import java.util.*;

class Solution {

    public int findCheapestPrice(
            int n,
            int[][] flights,
            int src,
            int dst,
            int k) {

        List<List<int[]>> adj = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] flight : flights) {

            int from = flight[0];
            int to = flight[1];
            int price = flight[2];

            adj.get(from).add(
                new int[]{to, price}
            );
        }

        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);

        dist[src] = 0;

        // {flightsUsed, node, cost}
        Queue<int[]> queue = new ArrayDeque<>();

        queue.offer(
            new int[]{0, src, 0}
        );

        while (!queue.isEmpty()) {

            int[] cur = queue.poll();

            int flightsUsed = cur[0];
            int node = cur[1];
            int cost = cur[2];

            // K stops = maximum K + 1 flights.
            if (flightsUsed == k + 1) {
                continue;
            }

            for (int[] edge : adj.get(node)) {

                int nei = edge[0];
                int price = edge[1];

                int newCost = cost + price;

                if (newCost < dist[nei]) {

                    dist[nei] = newCost;

                    queue.offer(
                        new int[]{
                            flightsUsed + 1,
                            nei,
                            newCost
                        }
                    );
                }
            }
        }

        return dist[dst] == Integer.MAX_VALUE
                ? -1
                : dist[dst];
    }
}