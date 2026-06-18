class Solution {
    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                if(find(board, word, 0, i, j, delRow, delCol))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean find(char[][] board, String word, int index, int row, int col, int[] delRow, int[] delCol)
    {
        if(board[row][col] != word.charAt(index))
        {
            return false;
        }

        if(index == word.length() - 1)
        {
            return true;
        }


        char curChar = board[row][col];
        board[row][col] = '*';

        for(int i = 0; i < 4; i++)
        {
            int nRow = row + delRow[i];
            int nCol = col + delCol[i];

            if(nRow >= 0 && nRow < board.length && nCol >= 0 && nCol < board[0].length)
            {
                if(find(board, word, index + 1, nRow, nCol, delRow, delCol))
                {
                    return true;
                }
            }
        }
        board[row][col] = curChar;

        return false;

    }
}