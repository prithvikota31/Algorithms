class Solution {
    public int numIslands(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, -1, 0, 1};
        int count = 0;
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(!visited[i][j] && grid[i][j] == '1')
                {
                    dfs(visited, grid, delRow, delCol, i, j);
                    count++;
                }
            }
        }
        return count;
        
    }

    public void dfs(boolean[][] visited, char[][] grid, int[] delRow, int[] delCol, int row, int col)
    {
        visited[row][col] = true;

        for(int i = 0; i < delRow.length; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < grid.length && nCol >= 0 && nCol < grid[0].length &&
                !visited[nRow][nCol] && grid[nRow][nCol] == '1')
            {
                dfs(visited, grid, delRow, delCol, nRow, nCol);
            }
        }
    }
}