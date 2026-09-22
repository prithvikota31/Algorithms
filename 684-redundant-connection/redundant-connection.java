class Solution {
    int[] parent;
    int[] size;
    public int[] findRedundantConnection(int[][] edges) {
        //n edges for n nodes
        // 1 extra connection
        int n = edges.length;
        parent = new int[n];
        for(int i = 0; i < n; i++)
        {
            parent[i] = i;
        }
        size = new int[n];

        for(int[] edge: edges)
        {
            int u = edge[0] - 1;
            int v = edge[1] - 1;
            if(!union(u, v))
            {
                return edge;
            }
        }

        return new int[0];

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