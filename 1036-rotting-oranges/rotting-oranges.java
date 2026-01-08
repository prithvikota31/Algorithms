class Solution {
    public int orangesRotting(int[][] grid) {
        int time = bfs(grid);
        return time;
    }

    public int bfs(int[][] grid)
    {
        Deque<Pair> q = new ArrayDeque<>();
        int time = 0;
        int fresh = 0;
        //add all rotten oranges into queue
        for(int i = 0; i < grid.length; i++)
        {
            for(int j = 0; j < grid[0].length; j++)
            {
                if(grid[i][j] == 2)
                    q.offer(new Pair(i, j));
                else if(grid[i][j] == 1)
                    fresh++;

            }
        }

        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, 1, 0, -1};

        while(!q.isEmpty())
        {
            int qSize = q.size();
            for(int c = 0; c < qSize; c++)
            {
                Pair cNode = q.poll();
                int cRow = cNode.x;
                int cCol = cNode.y;

                for(int i = 0; i < 4; i++)
                {
                    int nRow = cRow + delRow[i];
                    int nCol = cCol + delCol[i];

                    if(nRow >= 0 && nRow < grid.length && nCol >= 0 && nCol < grid[0].length &&
                        grid[nRow][nCol] == 1)
                    {
                        grid[nRow][nCol] = 2;
                        q.offer(new Pair(nRow, nCol));
                        fresh--;
                    }
                }           
            }

            if(!q.isEmpty())  time++;
        }
        return fresh == 0? time: -1;
    }

}

class Pair
{
    int x;
    int y;
    public Pair(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}