class Solution {
    private int[] parent;
    private int[] size;

    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;

        parent = new int[n + 1];
        size = new int[n + 1];

        // Each node starts as its own component.
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // If two nodes already have the same ultimate parent,
        // adding this edge creates a cycle.
        for (int[] edge : edges) {
            if (!union(edge[0], edge[1])) {
                return edge;
            }
        }

        return new int[0];
    }

    private int find(int node) {
        if (parent[node] == node) {
            return node;
        }

        // Path compression: directly attach node to ultimate parent.
        parent[node] = find(parent[node]);
        return parent[node];
    }

    private boolean union(int u, int v) {
        int pu = find(u);
        int pv = find(v);

        if (pu == pv) {
            return false;
        }

        // Union by size: attach smaller component under larger component.
        if (size[pu] < size[pv]) {
            parent[pu] = pv;
            size[pv] += size[pu];
        } else {
            parent[pv] = pu;
            size[pu] += size[pv];
        }

        return true;
    }
}