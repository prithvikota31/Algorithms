class Solution {
    
    public int maxAreaOfIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, -1, 0, 1};
        int[] count = new int[1];
        int maxArea = 0;
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(!visited[i][j] && grid[i][j] == 1)
                {
                    count[0] = 0;
                    dfs(visited, grid, delRow, delCol, i, j, count);
                    maxArea = Math.max(maxArea, count[0]);
                }
            }
        }
        return maxArea;
        
    }

    public void dfs(boolean[][] visited, int[][] grid, int[] delRow, int[] delCol, int row, int col, int[] count)
    {
        visited[row][col] = true;
        count[0]++;
        for(int i = 0; i < delRow.length; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < grid.length && nCol >= 0 && nCol < grid[0].length &&
                !visited[nRow][nCol] && grid[nRow][nCol] == 1)
            {
                dfs(visited, grid, delRow, delCol, nRow, nCol, count);
            }
        }
    }
}