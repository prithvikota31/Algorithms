class Solution {
    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};    
        int[][] visited = new int[m][n];
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j ++)
            {
                if(board[i][j] == word.charAt(0))
                {
                    if(dfs(1, board, delRow, delCol, word, i, j, visited))   return true;
                }
            }
        }

        return false;
    }

    //"s e e"
    public boolean dfs(int ind, char[][] board, int[] delRow, int[] delCol, String word, int row, int col,
    int[][] visited)
    {
        visited[row][col] = 1;
        if(ind == word.length())
        {
            visited[row][col] = 0;
            return true;
        }
        //match ind before travsersing further
        for(int i = 0; i < 4; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < board.length && nCol >= 0 && nCol < board[0].length &&
            board[nRow][nCol] == word.charAt(ind) && visited[nRow][nCol] == 0)
            {
                if(dfs(ind + 1, board, delRow, delCol, word, nRow, nCol, visited))  
                {
                    visited[row][col] = 0;
                    return true;
                } 
            }
        }
        visited[row][col] = 0;
        return false;
    }
}