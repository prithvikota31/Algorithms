class Solution {
    public int minimumEffortPath(int[][] heights) {
        int m = heights.length;
        int n = heights[0].length;

        int[][] effortGrid = new int[m][n];

        for(int i = 0; i < m; i++)
        {
            Arrays.fill(effortGrid[i], (int)1e9);
        }

        effortGrid[0][0] = 0;
        //[effot, row, col]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        minHeap.offer(new int[]{0, 0, 0});

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};
        while(!minHeap.isEmpty())
        {
            int[] cur = minHeap.poll();
            int cEffort = cur[0];
            int cRow = cur[1];
            int cCol = cur[2];

            if(cRow == m - 1 && cCol == n - 1)
            {
                return cEffort;
            }

            for(int i = 0; i < delRow.length; i++)
            {
                int nRow = cRow + delRow[i];
                int nCol = cCol + delCol[i];

                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n)
                {

                    int diffEffort = Math.abs(heights[nRow][nCol] - heights[cRow][cCol]);
                    int nEffort = Math.max(diffEffort, cEffort);
                    if(nEffort < effortGrid[nRow][nCol])
                    {
                        effortGrid[nRow][nCol] = nEffort;
                        minHeap.offer(new int[]{nEffort, nRow, nCol});
                    }       
                }
            }
        }

        return 0;
    }
}