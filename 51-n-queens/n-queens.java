class Solution {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(board[i], '.');
        }

        Set<Integer> rows = new HashSet<>();
        Set<Integer> diag1 = new HashSet<>(); // row - col
        Set<Integer> diag2 = new HashSet<>(); // row + col

        backtrack(0, n, board, rows, diag1, diag2, result);
        return result;
    }

    private void backtrack(
        int col,
        int n,
        char[][] board,
        Set<Integer> rows,
        Set<Integer> diag1,
        Set<Integer> diag2,
        List<List<String>> result
    ) {
        // If all columns are filled, we placed n queens safely
        if (col == n) {
            result.add(buildBoard(board));
            return;
        }

        // For this column, try placing queen in every row
        for (int row = 0; row < n; row++) {
            int d1 = row - col;
            int d2 = row + col;

            // Unsafe if same row or diagonal is already occupied
            if (rows.contains(row) || diag1.contains(d1) || diag2.contains(d2)) {
                continue;
            }

            // Choose
            board[row][col] = 'Q';
            rows.add(row);
            diag1.add(d1);
            diag2.add(d2);

            // Explore next column
            backtrack(col + 1, n, board, rows, diag1, diag2, result);

            // Undo
            board[row][col] = '.';
            rows.remove(row);
            diag1.remove(d1);
            diag2.remove(d2);
        }
    }

    private List<String> buildBoard(char[][] board) {
        List<String> list = new ArrayList<>();

        for (char[] row : board) {
            list.add(new String(row));
        }

        return list;
    }
}