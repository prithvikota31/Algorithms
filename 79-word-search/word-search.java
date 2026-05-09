class Solution {

    int[] delRow = {1, 0, -1, 0};
    int[] delCol = {0, 1, 0, -1};

    public boolean exist(char[][] board, String word) {

        int rows = board.length;
        int cols = board[0].length;

        // Try every cell as starting point
        for(int row = 0; row < rows; row++) {

            for(int col = 0; col < cols; col++) {

                // First character matched
                if(board[row][col] == word.charAt(0)) {

                    if(dfs(board, word, row, col, 0)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean dfs(char[][] board,
                       String word,
                       int row,
                       int col,
                       int index) {

        // Entire word matched
        if(index == word.length()) {
            return true;
        }

        // Boundary check
        if(row < 0 || col < 0 ||
           row >= board.length ||
           col >= board[0].length) {
            return false;
        }

        // Character mismatch
        if(board[row][col] != word.charAt(index)) {
            return false;
        }

        // Mark visited
        char temp = board[row][col];
        board[row][col] = '#';

        // Explore all 4 directions
        for(int i = 0; i < 4; i++) {

            int newRow = row + delRow[i];
            int newCol = col + delCol[i];

            if(dfs(board, word, newRow, newCol, index + 1)) {
                return true;
            }
        }

        // Backtrack
        board[row][col] = temp;

        return false;
    }
}