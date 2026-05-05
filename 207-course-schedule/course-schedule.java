class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();

        for(int i = 0; i < numCourses; i++)
        {
            graph.add(new ArrayList<>());
        }

        int[] inDegree = new int[numCourses];
        for(int i = 0;i < prerequisites.length; i++)
        {
            graph.get(prerequisites[i][1]).add(prerequisites[i][0]);
            inDegree[prerequisites[i][0]]++;
        }

        Deque<Integer> q = new ArrayDeque<>();
        for(int i = 0; i < numCourses; i++)
        {
            if(inDegree[i] == 0)
                q.offer(i);
        }

        int count = 0;
        while(!q.isEmpty())
        {
            int node = q.poll();
            count++;

            for(int nei: graph.get(node))
            {
                inDegree[nei]--;
                if(inDegree[nei] == 0)
                {
                    q.offer(nei);
                }
            }
        }

        return count == numCourses;
    }
}