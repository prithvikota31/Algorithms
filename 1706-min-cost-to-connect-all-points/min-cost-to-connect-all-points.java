class Solution {

    public int minCostConnectPoints(int[][] points) {

        int n = points.length;

        boolean[] visited = new boolean[n];

        PriorityQueue<int[]> minHeap =
            new PriorityQueue<>((a,b) -> a[0] - b[0]);

        minHeap.add(new int[]{0, 0}); // cost, node

        int totalCost = 0;
        int nodesUsed = 0;

        while(nodesUsed < n) {

            int[] curr = minHeap.poll();

            int cost = curr[0];
            int node = curr[1];

            if(visited[node])
                continue;

            visited[node] = true;

            totalCost += cost;
            nodesUsed++;

            for(int i = 0; i < n; i++) {

                if(!visited[i]) {

                    int dist =
                        Math.abs(points[node][0] - points[i][0]) +
                        Math.abs(points[node][1] - points[i][1]);

                    minHeap.add(new int[]{dist, i});
                }
            }
        }

        return totalCost;
    }
}
