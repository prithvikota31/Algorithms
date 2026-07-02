class Solution {
    public int swimInWater(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};


        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        //int[] has time, row, col
        boolean[][] visited = new boolean[m][n];
        pq.offer(new int[]{grid[0][0], 0, 0});

        while(!pq.isEmpty())
        {
            int[] cur = pq.poll();
            int curRow = cur[1];
            int curCol = cur[2];
            int curTime = cur[0];

            if(curRow == n - 1 && curCol == n - 1)
            {
                return curTime;
            }
            if(visited[curRow][curCol])
            {
                continue;
            }

            visited[curRow][curCol] = true;

            for(int i = 0; i < delRow.length; i++)
            {
                int nRow = curRow + delRow[i];
                int nCol = curCol + delCol[i];

                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n && !visited[nRow][nCol])
                {
                    int nTime = Math.max(curTime, grid[nRow][nCol]);
                    pq.offer(new int[]{nTime, nRow, nCol});
                }
            }
        }
        return 0;
    }
}