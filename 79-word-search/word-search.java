class Solution {
    int[] delRow = {1, 0, -1, 0};
    int[] delCol = {0, 1, 0, -1};

    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;

        boolean[][] visited = new boolean[m][n];

        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(board[i][j] == word.charAt(0))
                {
                    if(dfs(board, visited, i, j, word, 1))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean dfs(char[][] board, boolean[][] visited, int row, int col, String word, int index)
    {
        if(index == word.length())
        {
            return true;
        }
        visited[row][col] = true;

        for(int i = 0; i < 4; i++)
        {
            int nr = row + delRow[i];
            int nc = col + delCol[i];

            if(nr >= 0 && nr < board.length && nc >= 0 &&
            nc < board[0].length && !visited[nr][nc] && word.charAt(index) == board[nr][nc])
            {
                if(dfs(board, visited, nr, nc, word, index + 1))
                {
                    return true;
                }
            }
        }

        visited[row][col] = false;

        return false;


    }



}