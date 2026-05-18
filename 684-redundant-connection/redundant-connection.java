class Solution {
    List<Integer> parent = new ArrayList<>();
    List<Integer> size = new ArrayList<>();

    //findParent and union functions are crucial
    //size always number of nodes connected to the node
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length; //number of nodes
        for(int i = 0; i <= n; i++)
        {
            size.add(1);
            parent.add(i);
        }


        int lastIndex = -1;
        for(int i = 0; i < edges.length; i++)
        {
            if(!union(edges[i][0], edges[i][1]))
            {
                lastIndex = i;
            }
        }

        return edges[lastIndex];

    }


    private boolean union(int u, int v)
    {
        int ulu = findUparent(u);
        int ulv = findUparent(v);
        if(ulu == ulv)
        {
            return false;
        }
        if(size.get(ulu) <= size.get(ulv))
        {
            //connect u to v
            parent.set(ulu, ulv);
            size.set(ulv, size.get(ulv) + size.get(ulv));
        }
        else
        {
            //connect v to u
            parent.set(ulv, ulu);
            size.set(ulu, size.get(ulu) + size.get(ulv));
        }
        return true;
    }

    private int findUparent(int node)
    {
        if(parent.get(node) == node)
        {
            return node;
        }

        int ulp = findUparent(parent.get(node));
        //path compression
        parent.set(node, ulp);
        return parent.get(node);
    }
}