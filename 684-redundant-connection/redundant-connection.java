class Solution {
    int[] parent;
    public int[] findRedundantConnection(int[][] edges) {
        int[] rank = new int[edges.length];
        int n = edges.length;
        parent = new int[n + 1];
        
        for(int i = 1; i <= n; i++)
        {
            parent[i] = i;
        }

        for(int i = 0; i < n; i++)
        {
            int u = edges[i][0];
            int v = edges[i][1];
            int uParentU = findUParent(u);
            int uParentV = findUParent(v);

            if(uParentU == uParentV)    return edges[i];

            union(u, v);
        }

        return new int[0];
    }


    public int findUParent(int node)
    {
        if(parent[node] == node)    return node;

        return parent[node] = findUParent(parent[node]);
    }

    public void union(int u, int v) //simple union not by rank or size
    {
        int uParentU = findUParent(u);
        int uParentV = findUParent(v);

        parent[uParentU] = uParentV;
    }


}