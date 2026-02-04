class Solution {

    int[] delRow = {-1, 1, 0, 0};
    int[] delCol = {0, 0, 1, -1};

    public boolean exist(char[][] board, String word) {
        int m = board.length, n = board[0].length;
        boolean[][] vis = new boolean[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfs(board, word, 0, i, j, vis))
                    return true;
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int idx,
                        int row, int col, boolean[][] vis) {

        // bounds + visited + mismatch
        if (row < 0 || col < 0 ||
            row >= board.length || col >= board[0].length ||
            vis[row][col] ||
            board[row][col] != word.charAt(idx))
            return false;

        // last character matched
        if (idx == word.length() - 1)
            return true;

        vis[row][col] = true;

        for (int d = 0; d < 4; d++) {
            int nr = row + delRow[d];
            int nc = col + delCol[d];

            if (dfs(board, word, idx + 1, nr, nc, vis)) {
                vis[row][col] = false;
                return true;
            }
        }

        vis[row][col] = false;
        return false;
    }
}
