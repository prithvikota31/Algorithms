class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();

        for(int i = 0; i < numCourses; i++)
        {
            graph.add(new ArrayList<>());
        }
        //[i, j] => j -> i
        int[] inDegree = new int[numCourses];
        for(int i = 0; i < prerequisites.length; i++)
        {
            graph.get(prerequisites[i][1]).add(prerequisites[i][0]);
            inDegree[prerequisites[i][0]]++;
        }


        int[] topoSort = new int[numCourses];
        Deque<Integer> q = new ArrayDeque<>();

        for(int i = 0; i < inDegree.length; i++)
        {
            if(inDegree[i] == 0)
            {
                q.offer(i);
            }         
        }
        int index = 0;
        while(!q.isEmpty())
        {
            int curNode = q.poll();
            topoSort[index++] = curNode;

            for(int nei: graph.get(curNode))
            {
                inDegree[nei]--;
                if(inDegree[nei] == 0)
                {
                    q.offer(nei);
                }
            }
        }

        if(index == numCourses)
        {
            return true;
        }
        else
        {
            return false;
        }


    }
}