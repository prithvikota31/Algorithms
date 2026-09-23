class Solution {
    int[] parent;
    int[] size;
    
    public int countComponents(int n, int[][] edges) {
        int connected = n;
        parent = new int[n];
        for(int i = 0; i < n; i++)
        {
            parent[i] = i;
        }
        size = new int[n];
        Arrays.fill(size, 1);

        for(int[] edge: edges)
        {
            if(union(edge[0], edge[1]))
            {
                connected--;
            }
        }
        return connected;

    }

    private int find(int node)
    {
        if(parent[node] == node)
        {
            return node;
        }

        parent[node] = find(parent[node]);
        return parent[node];
    }

    private boolean union(int u , int v)
    {
        int ulPu = find(u);
        int ulPv = find(v);

        if(ulPu == ulPv)
        {
            return false;
        }

        if(size[ulPu] >= size[ulPv])
        {
            //parent of v is u
            parent[ulPv] = ulPu;
            size[ulPu] = size[ulPu] + size[ulPv];
        }
        else
        {
            //parent of u is v
            parent[ulPu] = ulPv;
            size[ulPv] = size[ulPu] + size[ulPv];
        }
        return true;
    }
}