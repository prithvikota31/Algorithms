class Solution {
    int[] parent;
    int[] rank;
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        parent = new int[n + 1];
        rank = new int[n + 1];
        
        for(int i = 1; i <= n; i++)
        {
            parent[i] = i;
            rank[i] = 0;
        }

        for(int i = 0; i < n; i++)
        {
            int u = edges[i][0];
            int v = edges[i][1];
            int uParentU = findUParent(u);
            int uParentV = findUParent(v);

            if(uParentU == uParentV)    return edges[i];

            unionByRank(u, v, rank);
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
    
    public void unionByRank(int u, int v, int[] rank) //union by rank 
    {
        int uParentU = findUParent(u);
        int uParentV = findUParent(v);
        if(uParentU == uParentV)    return;
        if(rank[uParentU] < rank[uParentV])
        {
            parent[uParentU] = uParentV;
        }
        else if(rank[uParentU] > rank[uParentV])
        {
            parent[uParentV] = uParentU;
        }
        else
        {
            parent[uParentV] = uParentU;
            rank[uParentU] = rank[uParentU] + 1;
        }
    }


}