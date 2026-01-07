class Solution {
    public int numIslands(char[][] grid) {
        //each cell is a node
        //go with bfs
        int m = grid.length;
        int n = grid[0].length;
        int[][] visited = new int[m][n];

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};

        int count = 0;
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(visited[i][j] == 0 && grid[i][j] == '1')
                {
                    bfs(grid, visited, delRow, delCol, i , j);
                    count++;
                }
            }
        }
        return count;
    }

    public void bfs(char[][] grid, int[][] visited, int[] delRow, int[] delCol, int i, int j)
    {
        Queue<Pair> q = new LinkedList<>();
        visited[i][j] = 1;
        q.add(new Pair(i, j));
        int m = visited.length;
        int n = visited[0].length;

        while(!q.isEmpty())
        {
            Pair cur = q.poll();
            int curX = cur.x;
            int curY = cur.y;

            for(int it = 0; it < 4; it++)
            {
                int neighX = curX + delRow[it];
                int neighY = curY + delCol[it];

                if(neighX >= 0 && neighX <= m - 1 && neighY >= 0 && neighY <= n - 1 
                    && visited[neighX][neighY] == 0 && grid[neighX][neighY] == '1')
                {
                    visited[neighX][neighY] = 1;
                    q.add(new Pair(neighX, neighY));
                }
            }
        }
    }

}


class Pair{
    int x;
    int y;

    public Pair(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}