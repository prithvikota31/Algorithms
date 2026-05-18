class Solution {

    public boolean validTree(int n, int[][] edges) {

        // tree must have exactly n-1 edges
        if (edges.length != n - 1) {
            return false;
        }

        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] edge : edges) {

            int a = edge[0];
            int b = edge[1];

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        Queue<Integer> q = new LinkedList<>();

        boolean[] visited = new boolean[n];

        q.offer(0);

        visited[0] = true;

        while (!q.isEmpty()) {

            int node = q.poll();

            for (int nei : graph.get(node)) {

                if (!visited[nei]) {

                    visited[nei] = true;

                    q.offer(nei);
                }
            }
        }

        // all nodes must be connected
        for (boolean v : visited) {

            if (!v) {
                return false;
            }
        }

        return true;
    }
}