class Solution {
    public int orangesRotting(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int fresh = 0;
        Deque<int[]> q = new ArrayDeque<>(); //contains rotten
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(grid[i][j] == 1)
                {
                    fresh++;
                }
                else if(grid[i][j] == 2)
                {
                    q.offer(new int[]{i, j});
                }
            }
        }

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {-1, 0, 1, 0};

        int time = 0;
        while(!q.isEmpty())
        {
            int size = q.size();

            for(int i = 0; i < size; i++)
            {
                int[] cur = q.poll();

                for(int j = 0; j < 4; j++)
                {
                    int nRow = cur[0] + delRow[j];
                    int nCol = cur[1] + delCol[j];
                    if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n && grid[nRow][nCol] == 1)
                    {
                        grid[nRow][nCol] = 2;
                        q.offer(new int[]{nRow, nCol});
                        fresh--;
                    }
                }
            }

            if(!q.isEmpty())
            {
                time++;
            }
        }

        if(fresh == 0)
        {
            return time;
        }
        else
        {
            return -1;
        }

    }
}