class Solution {
    private int[] parent;
    private int[] size;


    public boolean validTree(int n, int[][] edges) {
        if (edges.length != n - 1) return false;
        parent = new int[n];
        size = new int[n];
        for(int i = 0; i < n; i++)
        {
            parent[i] = i;
            size[i] = 1;
        }

        for(int i = 0; i < edges.length; i++)
        {
            int a = edges[i][0];
            int b = edges[i][1];

            int uParentA = findUParent(a);
            int uParentB = findUParent(b);

            if(uParentA == uParentB)    return false;
            unionBySize(uParentA, uParentB);

        }

        return true;
    }

    
    private int findUParent(int node)
    {
        if(parent[node] == node)    return node;

        return parent[node] = findUParent(parent[node]);
    }

    private void unionBySize(int u, int v)
    {
        int uParentU = findUParent(u);
        int uParentV = findUParent(v);

        if(uParentU == uParentV)    return;
        parent[uParentU] = uParentV;
    }
}