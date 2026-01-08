class Solution {
    int m, n;
    int[] dr = {-1, 0, 1, 0};
    int[] dc = {0, -1, 0, 1};

    public void solve(char[][] board) {
        if (board.length == 0) return;
        m = board.length;
        n = board[0].length;

        boolean[][] visited = new boolean[m][n];

        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (board[i][j] == 'O' && !visited[i][j]) {
                    List<int[]> comp = new ArrayList<>();
                    boolean touchesBorder = dfs(board, visited, i, j, comp);

                    if (!touchesBorder) {
                        for (int[] p : comp) {
                            board[p[0]][p[1]] = 'X';
                        }
                    }
                }
            }
        }
    }

    private boolean dfs(char[][] board, boolean[][] visited,
                        int r, int c, List<int[]> comp) {

        visited[r][c] = true;
        comp.add(new int[]{r, c});

        boolean touchesBorder =
                (r == 0 || r == m - 1 || c == 0 || c == n - 1);

        for (int k = 0; k < 4; k++) {
            int nr = r + dr[k];
            int nc = c + dc[k];

            if (nr >= 0 && nr < m && nc >= 0 && nc < n
                    && !visited[nr][nc] && board[nr][nc] == 'O') {
                touchesBorder |= dfs(board, visited, nr, nc, comp);
            }
        }
        return touchesBorder;
    }
}
