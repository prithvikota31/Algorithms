class Solution {
    public boolean exist(char[][] board, String word) {
        //dfs and backtracking
        //index corresponds to word pointer

        int[][] vis = new int[board.length][board[0].length];

        // if(board.length == 1 && board[0].length == 1)
        // {
        //     if(word.length() == 1)
        //     {
        //         if(word.charAt(0) == board[0][0])
        //             return true;
        //     }

        //     return false;
        // }
    

        int[] delRow = {-1, 1, 0, 0};
        int[] delCol = {0, 0, 1, -1};

        //dfs path 4 directions(-1, 0)
        //(1, 0)
        //(0, 1)
        //(0 , -1)
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                if(word.charAt(0) == board[i][j])
                    {if(dfs(board, word, 0, vis, i, j, delRow, delCol))
                        return true;}               
            }
           
        }
        return false;

    }

public boolean dfs(char[][] board, String word, int index, int[][] vis, int row, int col, int[] delRow, int[] delCol)
{
    
    if(index == word.length())
        return true;

    if(row >=0 && row < board.length && col >=0 && col < board[0].length && vis[row][col] == 0)
    {
        vis[row][col] = 1;
    }
    else
    {
        return false;
    }
    




    if(word.charAt(index) == board[row][col])
    {
        for(int i = 0; i < 4; i++)
        {
            int curRow = row + delRow[i];
            int curCol = col + delCol[i];

            if(dfs(board, word, index + 1, vis, curRow, curCol, delRow, delCol))
            {
                vis[row][col] = 0;
                return true;
            }
        }                 
    }
    vis[row][col] = 0;
    return false;

}
}
