class Solution {
    public void wallsAndGates(int[][] rooms) {
        int m = rooms.length;
        int n = rooms[0].length;

        Deque<int[]> q = new ArrayDeque<>();
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(rooms[i][j] == 0)
                {
                    q.offer(new int[]{i, j});
                }
            }
        }

        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, -1, 0, 1};

        while(!q.isEmpty())
        {
            int[] cur = q.poll();
            int curRow = cur[0];
            int curCol = cur[1];

            for(int i = 0; i < 4; i++)
            {
                int nRow = curRow + delRow[i];
                int nCol = curCol + delCol[i];

                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n && rooms[nRow][nCol] == Integer.MAX_VALUE)
                {
                    rooms[nRow][nCol] = rooms[curRow][curCol] + 1;
                    q.offer(new int[]{nRow, nCol});
                }
            }
        }
    }
}