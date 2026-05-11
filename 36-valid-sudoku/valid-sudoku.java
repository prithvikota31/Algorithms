class Solution {
    public boolean isValidSudoku(char[][] board) {
        boolean[][] rows = new boolean[9][9];
        boolean[][] cols = new boolean[9][9];
        boolean[][] boxes = new boolean[9][9];

        //row[i][j] corresponds to, in ith row/col/box, correponding number j exists or not
        //j will be converted to 0 index

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                if(board[i][j] == '.')
                {
                    continue;
                }
                char ch = board[i][j]; //ith row, jth col
                int number = ch - '1'; //0-indexed;

                int boxIndex = (i / 3) * 3 + (j /3);
                if(rows[i][number] ||  cols[j][number] || boxes[boxIndex][number])
                {
                    return false;
                }

                rows[i][number]  = true;
                cols[j][number] = true;
                boxes[boxIndex][number] = true;
            }
        }

        return true;
    }
}