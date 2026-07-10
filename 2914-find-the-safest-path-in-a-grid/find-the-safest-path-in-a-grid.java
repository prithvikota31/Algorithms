class Solution {
    public int maximumSafenessFactor(List<List<Integer>> grid) {
        //atleast one thief in the gridMatrix

        //create a gridMatrix of cells, with safeyFactor of each cell
        //sf(i, j) = Min(abs(ti - i) + abs(tj - j) 

        //similar to rotten oranges for safety as it goes through bfs levelwise, first one reaches is the min safety
        //use src and dest
        //src(0,0) and dest(n - 1, n - 1)        
        int m = grid.size();
        int n = grid.get(0).size();
        int[][] gridMatrix = new int[m][n];


        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                gridMatrix[i][j] = grid.get(i).get(j);
            }
        }
        




        if(gridMatrix[0][0] == 1 || gridMatrix[m - 1][n - 1] == 1)
        {
            return 0;
        }

        // this contains (i, j) 
        // no need of dist as the gridMatrix itself mutates from 0 to dist
        int[][] safetyMatrix = new int[m][n];
        for(int i = 0; i < m; i++)
        {
            Arrays.fill(safetyMatrix[i], -1);
        }
        Deque<int[]> q = new ArrayDeque<>(); 
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(gridMatrix[i][j] == 1)
                {
                    q.offer(new int[]{i, j});
                    safetyMatrix[i][j] = 0;
                }
            }
        }
        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};
        
        while(!q.isEmpty())
        {
            int[] cur = q.poll();
            int cRow = cur[0];
            int cCol = cur[1];

            for(int i = 0; i < delRow.length; i++)
            {
                int nRow = cRow + delRow[i];
                int nCol = cCol + delCol[i];


                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n && safetyMatrix[nRow][nCol] == -1)
                {
                    safetyMatrix[nRow][nCol] = safetyMatrix[cRow][cCol] + 1;
                    q.offer(new int[]{nRow, nCol});
                }
            }
        }

        //maxPossibleSafetyMatrix
        int[][] maxPossibleSafety = new int[m][n];

        PriorityQueue<int[]> maxQueue = new PriorityQueue<>((a, b) -> (b[0] - a[0]));
        maxPossibleSafety[0][0] = safetyMatrix[0][0];
        maxQueue.offer(new int[]{safetyMatrix[0][0], 0, 0});

        while(!maxQueue.isEmpty())
        {
            int[] cur = maxQueue.poll();
            int cSafety = cur[0];
            int cRow = cur[1];
            int cCol = cur[2];

            // if (cSafety < maxPossibleSafety[cRow][cCol]) {
            //     continue;
            // }

            // if(cRow == m - 1 && cCol == n - 1)
            // {
            //     return cSafety;
            // }

            for(int i = 0; i < delRow.length; i++)
            {
                int nRow = cRow + delRow[i];
                int nCol = cCol + delCol[i];

                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n)
                {
                    int nSafety = Math.min(safetyMatrix[nRow][nCol], cSafety);

                    if(nSafety > maxPossibleSafety[nRow][nCol])
                    {
                        maxPossibleSafety[nRow][nCol] = nSafety;
                        maxQueue.offer(new int[]{nSafety, nRow, nCol});
                    }         
                }
            }
        }


        return maxPossibleSafety[m - 1][n - 1];


    }
}