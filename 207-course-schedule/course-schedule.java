class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        //[0, 1] => 1 -> 0
        // number of graph nodes = numCourses
        for(int i = 0; i < numCourses; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < prerequisites.length; i++)
        {
            graph.get(prerequisites[i][1]).add(prerequisites[i][0]);
        }

        //lets use detecting a cycle in directed graph to confirm 
        //If cycle detected - false;
        //else - true

        int[] visited = new int[graph.size()];
        int[] pathVis = new int[graph.size()]; 

        for(int i = 0; i < numCourses; i++)
        {
            if(visited[i] == 0)
            {
                if(dfsIsCycle(graph, i, visited, pathVis))
                {
                    return false;
                }
            }
        }

        return true;

        
    }


    public boolean dfsIsCycle(List<List<Integer>> graph, int node, int[] visited, int[] pathVis)
    {
        visited[node] = 1;
        pathVis[node] = 1;
        for(int nei: graph.get(node))
        {
            if(visited[nei] == 0)
            {
                if(dfsIsCycle(graph, nei, visited, pathVis))
                {
                    return true;
                }
            }

            else if(pathVis[nei] == 1)
            {
                return true;
            }
        }
        pathVis[node] = 0;
        return false;
    }
}