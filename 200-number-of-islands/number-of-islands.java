class Solution {
    public int numIslands(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, 1, 0, -1};
        int count = 0;

        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(!visited[i][j] && grid[i][j] == '1')
                {
                    dfs(grid, visited, i, j, delRow, delCol);
                    count++;
                }
            }
        }

        return count;
    }


    public void dfs(char[][] grid, boolean[][] visited , int r, int c, int[] delRow, int[] delCol)
    {
        visited[r][c] = true;

        for(int i = 0; i < 4; i++)
        {
            int nRow = delRow[i] + r;
            int nCol = delCol[i] + c;

            if(nRow >= 0 && nRow < grid.length && nCol >= 0 && nCol < grid[0].length &&
                !visited[nRow][nCol] && grid[nRow][nCol] == '1')
            {
                dfs(grid, visited, nRow, nCol, delRow, delCol);
            }
        }
    }
}