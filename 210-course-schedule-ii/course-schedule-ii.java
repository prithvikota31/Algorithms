class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) 
    {
        // the problem is to find a topological sort if it exists or not
        //first create a graph
        List<List<Integer>> graph = new LinkedList<>();
        int v = numCourses;
        //create an empty graph
        for(int i = 0; i < numCourses; i++)
        {
            graph.add(new LinkedList<>());
        }

        for(int i = 0; i < prerequisites.length; i++)
        {
            graph.get(prerequisites[i][1]).add(prerequisites[i][0]);
        }
        // till now graph is created
        int[] inDegree = new int[v];
        for(int i = 0; i < v; i++)
        {
            for(int it: graph.get(i))
            {
                inDegree[it]++;
            }
        }

        return topoSort(graph, v, inDegree);
    }


    public int[] topoSort(List<List<Integer>> graph, int v, int[] inDegree)
    {
        Deque<Integer> q = new ArrayDeque<>();

        //add all nodes with 0 indegree
        for(int i = 0; i < inDegree.length; i++)
        {
            if(inDegree[i] == 0)
                q.add(i);
        }
        int[] topoSort = new int[v];
        int ind = 0;

        while(!q.isEmpty())
        {
            int curNode = q.poll();
            topoSort[ind++] = curNode;

            for(Integer it : graph.get(curNode))
            {
                inDegree[it]--;
                if(inDegree[it] == 0)
                {
                    q.add(it);
                }
            }
        }

        if(ind == v)    return topoSort;
        else return new int[0];
    }
}