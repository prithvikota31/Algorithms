class Solution {
    public void solve(char[][] board) {
        int m = board.length;
        int n = board[0].length;
        boolean[][] visited = new boolean[m][n];

        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, -1, 0, 1};

        //mark all cells reachable from edge 'O's
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if((i == 0 || i == m - 1 || j == 0 || j == n - 1) && board[i][j] == 'O')
                {
                    //dfs
                    dfs(visited, board, delRow, delCol, i, j);

                }
            }
        }

        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(!visited[i][j] && board[i][j] == 'O')
                {
                    board[i][j] = 'X';
                }
            }
        }

    }

    public void dfs(boolean[][] visited, char[][] grid, int[] delRow, int[] delCol, int row, int col)
    {
        visited[row][col] = true;
        for(int i = 0; i < delRow.length; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < visited.length && nCol >= 0 && nCol < visited[0].length && !visited[nRow][nCol]
                && grid[nRow][nCol] == 'O')
            {
                dfs(visited, grid, delRow, delCol, nRow, nCol);
            }
        }
    }


}