class Solution {
    public int orangesRotting(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int fresh = 0;
        Deque<int[]> q = new ArrayDeque<>(); // {row, col, time}

        // Add all rotten oranges as BFS sources at time = 0.
        // Count fresh oranges so we know whether all can be rotted.
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    fresh++;
                } else if (grid[i][j] == 2) {
                    q.offer(new int[]{i, j, 0});
                }
            }
        }

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {-1, 0, 1, 0};

        int time = 0;

        while (!q.isEmpty()) {
            int[] cur = q.poll();

            int curRow = cur[0];
            int curCol = cur[1];
            int curTime = cur[2];

            // Track the farthest minute reached by BFS.
            time = curTime;

            for (int i = 0; i < 4; i++) {
                int nRow = curRow + delRow[i];
                int nCol = curCol + delCol[i];

                // Fresh orange becomes rotten at curTime + 1.
                if (nRow >= 0 && nRow < m &&
                    nCol >= 0 && nCol < n &&
                    grid[nRow][nCol] == 1) {

                    grid[nRow][nCol] = 2;
                    fresh--;
                    q.offer(new int[]{nRow, nCol, curTime + 1});
                }
            }
        }

        return fresh == 0 ? time : -1;
    }
}