class Solution {
    public void wallsAndGates(int[][] rooms) {
        int m = rooms.length;
        int n = rooms[0].length;

        Deque<Pair> q = new ArrayDeque<>();

        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(rooms[i][j] == 0)
                {
                    q.offer(new Pair(i, j));
                }
            }
        }
        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};

        while(!q.isEmpty())
        {
            Pair cur = q.poll();
            int curRow = cur.row;
            int curCol = cur.col;

            for(int i = 0; i < 4; i++)
            {
                int nRow = curRow + delRow[i];
                int nCol = curCol + delCol[i];

                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n && 
                    rooms[nRow][nCol] == Integer.MAX_VALUE)
                {
                    q.offer(new Pair(nRow, nCol));
                    rooms[nRow][nCol] = 1 + rooms[curRow][curCol];
                }
            }
        }

        return;
    }



}

class Pair{
    int row;
    int col;
    int distance;
    public Pair(int r, int c)
    {
        this.row = r;
        this.col = c;
    }
}