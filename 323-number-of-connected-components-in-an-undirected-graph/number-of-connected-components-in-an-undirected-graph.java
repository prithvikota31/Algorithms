class Solution {
    public int countComponents(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < edges.length; i++)
        {
            graph.get(edges[i][0]).add(edges[i][1]);
            graph.get(edges[i][1]).add(edges[i][0]);
        }

        int count = 0;
        boolean[] visited = new boolean[n];
        for(int i = 0; i < visited.length; i++)
        {
            if(!visited[i])
            {
                //dfs
                dfs(i, visited, graph);
                count++;
            }
        }

        return count;
    }


    public void dfs(int node, boolean[] visited, List<List<Integer>> graph)
    {
        visited[node] = true;

        for(int v : graph.get(node))
        {
            if(!visited[v])
            {
                dfs(v, visited, graph);
            }
        }
    }
}