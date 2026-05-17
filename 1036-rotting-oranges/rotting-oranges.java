class Solution {
    public int orangesRotting(int[][] grid) {
        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, -1, 0, 1};

        int m = grid.length;
        int n = grid[0].length;
        
        int fresh = 0;
        Deque<Pair> q= new ArrayDeque<>();
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(grid[i][j] == 1)
                {
                    fresh++;
                }
                if(grid[i][j] == 2)
                {
                    q.offer(new Pair(i, j));
                }
            }
        }

        int time = 0;
        int rotten = 0;

        while(!q.isEmpty())
        {
            int size = q.size();
            for(int i = 0; i < size; i++)
            {
                Pair cur = q.poll();
                int cRow = cur.row;
                int cCol = cur.col;

                for(int j = 0; j < 4; j++)
                {
                    int nRow = cRow + delRow[j];
                    int nCol = cCol + delCol[j];
                    if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n && grid[nRow][nCol] == 1)
                    {
                        grid[nRow][nCol] = 2;
                        q.offer(new Pair(nRow, nCol));
                        rotten++;
                    }
                }
            }

            if(!q.isEmpty())
            {
                time++;
            }
        }

        if(fresh != rotten) return -1;
        else return time;
    }

}

class Pair{
    int row;
    int col;

    public Pair(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
}