class Solution {


    public int maxAreaOfIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};

        int[][] visited = new int[m][n];

        int area = 0;
        int maxArea = 0;
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(visited[i][j] == 0 && grid[i][j] == 1)
                {
                    area = 0;
                    area = dfs(grid, visited, i, j, delRow, delCol);
                    maxArea = Math.max(maxArea, area);
                }
            }
        }
        return maxArea;
    }

    private int dfs(int[][] grid, int[][] visited, int row, int col, int[] delRow, int[] delCol)
    {
        visited[row][col] = 1;
        int area = 1;
        for(int i = 0; i < 4; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < grid.length && nCol >= 0 && nCol < grid[0].length 
                && grid[nRow][nCol] == 1 && visited[nRow][nCol] == 0)
            {
                area += dfs(grid, visited, nRow, nCol, delRow, delCol);
            }
        }

        return area;
    }
}