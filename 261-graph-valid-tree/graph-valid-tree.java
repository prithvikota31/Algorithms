class Solution {
    public boolean validTree(int n, int[][] edges) {
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

            int[] visited = new int[n];
            Deque<int[]> q = new ArrayDeque<>();
            q.offer(new int[]{0, -1});
            visited[0] = 1;


            while(!q.isEmpty())
            {
                int[] curNode = q.poll();
                int curValue = curNode[0];
                int curParent = curNode[1];
                for(int nei: graph.get(curValue))
                {
                    if(visited[nei] == 0)
                    {
                        q.offer(new int[]{nei, curValue});
                        visited[nei] = 1;
                    }
                    else if(nei != curParent)
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

