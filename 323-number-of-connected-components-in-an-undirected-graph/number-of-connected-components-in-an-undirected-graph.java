class Solution {
    private List<Integer> size, parent;
    public int countComponents(int n, int[][] edges) {
        size = new ArrayList<>();
        parent = new ArrayList<>();
        for(int i = 0; i <= n; i++)
        {
            size.add(1);
            parent.add(i);
        }

        int numConnected = n; //start with n


        for(int i = 0; i < edges.length; i++)
        {
            if(unionBySize(edges[i][0], edges[i][1]))
            {
                numConnected--;
            }
        }

        return numConnected;
    }


    private int findUParent(int node)
    {
        if(parent.get(node) == node)
        {
            return node;
        }

        int ulp = findUParent(parent.get(node));
        parent.set(node, ulp);
        return parent.get(node);
    }

    private boolean unionBySize(int u, int v)
    {
        int ulPu = findUParent(u);
        int ulPv = findUParent(v);
        if(ulPu == ulPv)
        {
            return false;
        }

        if(size.get(ulPu) <= size.get(ulPv))
        {
            parent.set(ulPu, ulPv);
            size.set(ulPv, size.get(ulPu) + size.get(ulPv));
        }
        else
        {
            parent.set(ulPv, ulPu);
            size.set(ulPu, size.get(ulPu) + size.get(ulPv));
        }

        return true;
    }



}