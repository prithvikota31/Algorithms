/*
 * ============================================================================
 * Striver Graphs #3 — Shortest Path in a Binary Maze (unit-weight grid)
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given a grid of 0s and 1s, a source cell and a destination cell, return the
 * length of the shortest path (number of steps) from source to destination.
 * You may move only through cells with value 1, one step in the 4 cardinal
 * directions. Return -1 if the destination is unreachable.
 *
 *   grid[r][c] == 1  -> walkable,   grid[r][c] == 0 -> wall
 *   source / destination: {row, col}
 *   return: step count, or -1.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Every move costs the SAME (1). On a uniform-weight graph, plain BFS from the
 * source already visits cells in non-decreasing distance order, so the FIRST
 * time you reach a cell is its shortest distance — no min-heap needed. That is
 * exactly why this is BFS (a FIFO queue), not Dijkstra.
 *
 * The dist[][] grid doubles as the visited guard: a cell is enqueued only when
 * its distance improves, which (for unit weights) happens exactly once. So each
 * cell enters the queue at most once.
 *
 * COMPLEXITY
 *   Time : O(m * n)     Space : O(m * n)
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class ShortestPathBinaryMaze {

    int shortestPath(int[][] grid, int[] source, int[] destination) {
        Deque<int[]> q = new ArrayDeque<>();

        //No need of pq as every neighbour it reaches with uniform distance
        int m = grid.length;
        int n = grid[0].length;
        int[][] dist = new int[m][n];

        for(int i = 0; i < m; i++)
        {
            Arrays.fill(dist[i], (int)1e9);
        }
        int sRow = source[0];
        int sCol = source[1];
        dist[sRow][sCol] = 0;

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};
        q.offer(new int[]{0, sRow, sCol});
        while(!q.isEmpty())
        {
            int[] cur = q.poll();
            int cDist = cur[0];
            int cRow = cur[1];
            int cCol = cur[2];

            if(cRow == destination[0] && cCol == destination[1])
            {
                return cDist;
            }

            //travers current nei
            for(int i = 0; i < delRow.length; i++)
            {
                int nRow = cRow + delRow[i];
                int nCol = cCol + delCol[i];

                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n && grid[nRow][nCol] == 1)
                {
                    int nDist = cDist + 1;
                    //relax the distance
                    if(nDist < dist[nRow][nCol])
                    {
                        dist[nRow][nCol] = nDist;
                        q.offer(new int[]{nDist, nRow, nCol});
                    }
                }
            }
        }
        return -1;
    }

    // ---------------------------------------------------------------------
    // Quick self-test.
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        ShortestPathBinaryMaze sol = new ShortestPathBinaryMaze();

        // 1) Open path around a wall.
        //    S . .
        //    # # .
        //    . . D
        int[][] g1 = {
            {1, 1, 1},
            {0, 0, 1},
            {1, 1, 1},
        };
        System.out.println(sol.shortestPath(g1, new int[]{0,0}, new int[]{2,2}));
        // expected: 4  (0,0)->(0,1)->(0,2)->(1,2)->(2,2)

        // 2) Destination walled off -> unreachable.
        int[][] g2 = {
            {1, 0, 1},
            {0, 0, 1},
            {1, 1, 1},
        };
        System.out.println(sol.shortestPath(g2, new int[]{0,0}, new int[]{0,2}));
        // expected: -1

        // 3) Source == destination.
        int[][] g3 = { {1} };
        System.out.println(sol.shortestPath(g3, new int[]{0,0}, new int[]{0,0}));
        // expected: 0
    }
}
