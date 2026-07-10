class Solution {
    public int minimumEffortPath(int[][] heights) {
        
        int m = heights.length;
        int n = heights[0].length;
        //sorted based on min effort travelled so far
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (a[0] - b[0]));
        int[][] effortGrid = new int[m][n];

        //to move from (i, j) to next, it is abs(difference in heights)
        effortGrid[0][0] = 0;
        for(int i = 0; i < m; i++)
        {
            Arrays.fill(effortGrid[i], (int)1e9);
        }

        pq.offer(new int[]{0, 0, 0});

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};
        while(!pq.isEmpty())
        {
            int[] cur = pq.poll();
            int cEffort = cur[0];
            int cRow = cur[1];
            int cCol = cur[2];

            if(cEffort > effortGrid[cRow][cCol])
            {
                continue;
            }

            if(cRow == m -1 && cCol == n - 1)
            {
                return cEffort;
            }

            for(int i = 0; i < 4; i++)
            {
                int nRow = cRow + delRow[i];
                int nCol = cCol + delCol[i];

                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n)
                {
                    int nEffort = Math.max(cEffort, Math.abs(heights[nRow][nCol] - heights[cRow][cCol]));
                    if(nEffort < effortGrid[nRow][nCol])
                    {
                        effortGrid[nRow][nCol] = nEffort;
                        pq.offer(new int[]{nEffort, nRow, nCol});
                    }
                }
            }
        }

        return effortGrid[m - 1][n - 1];
    }
}