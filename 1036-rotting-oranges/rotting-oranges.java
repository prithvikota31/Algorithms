class Solution {
    public int orangesRotting(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Deque<Pair> q = new ArrayDeque<>();
        int fresh = 0;

        // init
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 2) q.offer(new Pair(i, j));
                if (grid[i][j] == 1) fresh++;
            }
        }

        if (fresh == 0) return 0;

        int time = 0;
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};

        while (!q.isEmpty() && fresh > 0) {
            int size = q.size();
            time++;

            for (int k = 0; k < size; k++) {
                Pair p = q.poll();
                for (int d = 0; d < 4; d++) {
                    int r = p.x + dr[d];
                    int c = p.y + dc[d];
                    if (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] == 1) {
                        grid[r][c] = 2;
                        fresh--;
                        q.offer(new Pair(r, c));
                    }
                }
            }
        }

        return fresh == 0 ? time : -1;
    }
}

class Pair {
    int x, y;
    Pair(int x, int y) { this.x = x; this.y = y; }
}
