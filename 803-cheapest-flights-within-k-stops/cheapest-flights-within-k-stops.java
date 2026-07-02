class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        for (int[] f : flights) {
            graph.get(f[0]).add(new int[]{f[1], f[2]});
        }

        // dist[flightsUsed][city] = cheapest cost to reach city using this many flights
        int[][] dist = new int[k + 2][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{src, 0, 0}); // city, cost, flightsUsed
        dist[0][src] = 0;

        int ans = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int city = cur[0];
            int cost = cur[1];
            int flightsUsed = cur[2];

            if (city == dst) {
                ans = Math.min(ans, cost);
            }

            // k stops = at most k + 1 flights
            if (flightsUsed == k + 1) {
                continue;
            }

            for (int[] nei : graph.get(city)) {
                int nextCity = nei[0];
                int price = nei[1];

                int nextCost = cost + price;
                int nextFlights = flightsUsed + 1;

                if (nextCost < dist[nextFlights][nextCity]) {
                    dist[nextFlights][nextCity] = nextCost;
                    q.offer(new int[]{nextCity, nextCost, nextFlights});
                }
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}