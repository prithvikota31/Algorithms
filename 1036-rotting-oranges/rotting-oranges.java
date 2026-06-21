class Solution {
    public int orangesRotting(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        Deque<int[]> q = new ArrayDeque<>();
        int fresh = 0;

        // Put all initially rotten oranges into queue.
        // This is multi-source BFS.
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) {
                    q.offer(new int[]{i, j});
                } else if (grid[i][j] == 1) {
                    fresh++;
                }
            }
        }

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {-1, 0, 1, 0};

        int time = 0;

        // Each BFS level = 1 minute.
        while (!q.isEmpty() && fresh > 0) {
            int size = q.size();

            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();

                for (int d = 0; d < 4; d++) {
                    int nRow = cur[0] + delRow[d];
                    int nCol = cur[1] + delCol[d];

                    if (nRow >= 0 && nRow < m &&
                        nCol >= 0 && nCol < n &&
                        grid[nRow][nCol] == 1) {

                        grid[nRow][nCol] = 2;
                        fresh--;
                        q.offer(new int[]{nRow, nCol});
                    }
                }
            }

            time++;
        }

        return fresh == 0 ? time : -1;
    }
}