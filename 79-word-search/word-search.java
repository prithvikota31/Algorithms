class Solution {
    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};
        boolean[][] visited = new boolean[m][n];

        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(dfs(i, j, delRow, delCol, word, 0, visited, board))
                {
                    return true;
                }
            }
        }

        return false;
    }


    private boolean dfs(int row, int col,
                 int[] delRow, int[] delCol, String word, int index, boolean[][] visited, char[][] board)

    {

        char ch = word.charAt(index);
        if(ch != board[row][col])
        {
            return false;
        }
        if(index == word.length() - 1)
        {
            return true;
        }

        visited[row][col] = true;

        for(int i = 0; i < delRow.length; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];
            if(nRow >= 0 && nRow < board.length && nCol >= 0 && nCol < board[0].length 
            && !visited[nRow][nCol])
            {
                boolean found = dfs(nRow, nCol, delRow, delCol, word, index + 1, visited, board);
                if(found)
                {
                    visited[row][col] = false;
                    return true;
                }
            }
        }

        visited[row][col] = false;
        return false;
    }
}