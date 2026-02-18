class Solution {
    private int[] parent;
    private int[] size;
    private int findUparent(int node)
    {
        if(parent[node] == node)    return node;

        return parent[node] = findUparent(parent[node]);
    }

    private void unionBySize(int u, int v)
    {
        int ulParentU = findUparent(u);
        int ulParentV = findUparent(v);

        if(ulParentU == ulParentV)  return;

        if(size[ulParentU] < size[ulParentV])   parent[ulParentU] = ulParentV;
        else if(size[ulParentU] > size[ulParentV]) parent[ulParentV] = ulParentU;
        else
        {
            parent[ulParentV] = ulParentU;
            size[ulParentU] =  size[ulParentU] +  size[ulParentV]; 
        }
    }

    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        //each account consider it as a node with its index
        // make them as disjoint sets and do unions as per common email
        int n = accounts.size(); 
        parent = new int[n];
        size = new int[n];

        for(int i = 0; i < n; i++)
        {
            parent[i] = i;
            size[i] = 1;
        }

        //make each email point to its account index
        Map<String, Integer> mapEmailToNode = new HashMap<>();

        for(int i = 0; i < accounts.size(); i++)
        {
            for(int j = 1; j < accounts.get(i).size(); j++)
            {
                String email = accounts.get(i).get(j);
                if(!mapEmailToNode.containsKey(email))
                {
                    mapEmailToNode.put(email, i);
                }
                else
                {
                    unionBySize(i, mapEmailToNode.get(email));
                }    
            }
        }

        //By now the map contains contains email to its corresponding node index
        //Also nodes are union'd if there's a common email

        //Next, create the merged account for each thing, keeping some empty

        List<List<String>> iMergedAccounts = new ArrayList<>(); //intermediate merged accounts

        for(int i = 0; i < n; i++)
        {
            iMergedAccounts.add(new ArrayList<>());
        }

        for(Map.Entry<String, Integer> e: mapEmailToNode.entrySet())
        {
            String email = e.getKey();
            int uParent = findUparent(e.getValue());
            iMergedAccounts.get(uParent).add(email);
        }

        List<List<String>> finalMergedAccounts = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            if(iMergedAccounts.get(i).size() == 0)    continue;
            List<String> temp = new ArrayList<>();
            //add the name corresponding to the index
            temp.add(accounts.get(i).get(0));
            Collections.sort(iMergedAccounts.get(i));
            temp.addAll(iMergedAccounts.get(i));
            finalMergedAccounts.add(temp);
        }

        return finalMergedAccounts;

    }
}