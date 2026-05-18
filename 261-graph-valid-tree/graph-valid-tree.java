class Solution {
    public boolean validTree(int n, int[][] edges) {
        
        List<List<Integer>> graph = new ArrayList<>();

        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < edges.length; i++)
        {
            int a = edges[i][0];
            int b = edges[i][1];
            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        //now detect a cycle
        Deque<Pair> q = new ArrayDeque<>();
        int[] visited = new int[n];
        q.offer(new Pair(0, - 1));
        visited[0] = 1;
        while(!q.isEmpty())
        {
            Pair cur = q.poll();
            int cNode = cur.node;
            int parent = cur.parent;

            for(int i = 0; i < graph.get(cNode).size(); i++)
            {
                int nei = graph.get(cNode).get(i);
                if(visited[nei] == 0)
                {
                    visited[nei] = 1;
                    q.offer(new Pair(nei, cNode));
                }
                else if(nei != parent)
                {
                    return false;
                }
            }
        }

        for(int i = 0; i < n; i++)
        {
            if(visited[i] == 0)
            {
                return false;
            }
        }
        return true;

    }
}

class Pair{
    int node;
    int parent;

    public Pair(int node, int parent)
    {
        this.node = node;
        this.parent = parent;
    }
}