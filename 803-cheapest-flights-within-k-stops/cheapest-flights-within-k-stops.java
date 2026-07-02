class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
     
        // Create the adjacency list to represent airports and flights as a graph
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        
        // Add edges for the flights to the adjacency list
        for (int[] flight : flights) {
            adj.get(flight[0]).add(new int[]{flight[1], flight[2]});
        }

        // Create a queue to store the node, its distance from the source, and the number of stops
        Deque<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{0, src, 0});  // Push the source node with 0 stops and 0 cost

        // Create a distance array to store the minimum cost to reach each node
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        // BFS traversal with a queue to process the nodes
        while (!q.isEmpty()) {
            int[] current = q.poll();
            int stops = current[0];  // Number of stops so far
            int node = current[1];  // Current node
            int cost = current[2];  // Cost to reach the current node

            // If the number of stops exceeds K, continue to the next iteration
            if (stops > K)
                continue;

            // Iterate over all the adjacent nodes (next destinations)
            for (int[] adjNode : adj.get(node)) {
                int nextNode = adjNode[0];  // Next destination node
                int edW = adjNode[1];  // Cost of the flight to the next destination

                // If a shorter path to the adjacent node is found, update the distance
                if (cost + edW < dist[nextNode] && stops <= K) {
                    dist[nextNode] = cost + edW;  // Update the distance
                    q.offer(new int[]{stops + 1, nextNode, cost + edW});  // Push the new node with updated stops and cost
                }
            }
        }

        // If destination node is unreachable, return -1
        if (dist[dst] == Integer.MAX_VALUE)
            return -1;

        return dist[dst];  // Return the minimum cost to reach the destination
    
    }
}