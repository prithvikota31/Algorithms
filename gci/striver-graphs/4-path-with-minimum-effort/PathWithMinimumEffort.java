/*
 * ============================================================================
 * Striver Graphs #4 — Path With Minimum Effort (minimax path on a grid)
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given a grid `heights[m][n]`, travel from the top-left (0,0) to the
 * bottom-right (m-1,n-1) moving in the 4 cardinal directions. The EFFORT of a
 * path is the MAXIMUM absolute height difference between two consecutive cells
 * along that path. Return the minimum effort over all paths.  (LC 1631)
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * This is Dijkstra, but the path cost is a BOTTLENECK, not a sum. Classic
 * Dijkstra relaxes with `dist + weight`; here we relax with
 *
 *     newEffort = max(effortSoFar, |height(next) - height(cur)|)
 *
 * i.e. a path is only as good as its single worst step. A min-heap keyed by
 * effort still works: pop the cell reachable with the smallest bottleneck,
 * and the first time the destination pops, that effort is optimal (all
 * "weights" >= 0, so no later path can improve it).
 *
 * The effortGrid doubles as the visited/finalized guard via the stale-skip
 * `if (cEffort > effortGrid[cRow][cCol]) continue;`.
 *
 * COMPLEXITY
 *   Time : O(m * n * log(m * n))     Space : O(m * n)
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class PathWithMinimumEffort {

    public int MinimumEffort(List<List<Integer>> heights) {
        //minimize the difference along the path
        //when relaxing update the effort, only if maximum effort is less than the current reported effort

        int m = heights.size();
        int n = heights.get(0).size();
        int[][] effortGrid = new int[m][n];

        for(int i = 0; i < m; i++)
        {
            Arrays.fill(effortGrid[i], (int)1e9);
        }

        effortGrid[0][0] = 0;

        //int[] -> {effortSofar, row, col}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        pq.offer(new int[]{0, 0, 0});

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};

        while(!pq.isEmpty())
        {
            int[] cur = pq.poll();
            int cEffort = cur[0];
            int cRow = cur[1];
            int cCol = cur[2];

            if(cRow == m - 1 && cCol == n - 1)
            {
                return cEffort;
            }
            if(cEffort > effortGrid[cRow][cCol])
            {
                continue;
            }

            //travsere to neighbor
            for(int i = 0; i < delRow.length; i++)
            {
                int nRow = cRow + delRow[i];
                int nCol = cCol + delCol[i];

                if(nRow >= 0 && nRow < m && nCol >= 0 && nCol < n)
                {
                    int nEffort = 0;
                    nEffort =  Math.abs(heights.get(nRow).get(nCol) - heights.get(cRow).get(cCol));
                    nEffort = Math.max(cEffort, nEffort);


                    //relax the cell
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

    // ---------------------------------------------------------------------
    // Quick self-test.
    // ---------------------------------------------------------------------
    private static List<List<Integer>> grid(int[][] g) {
        List<List<Integer>> out = new ArrayList<>();
        for (int[] row : g) {
            List<Integer> r = new ArrayList<>();
            for (int v : row) r.add(v);
            out.add(r);
        }
        return out;
    }

    public static void main(String[] args) {
        PathWithMinimumEffort sol = new PathWithMinimumEffort();

        // 1) Route down-then-right keeps every step <= 2.
        int[][] g1 = {
            {1, 2, 2},
            {3, 8, 2},
            {5, 3, 5},
        };
        System.out.println(sol.MinimumEffort(grid(g1))); // expected: 2

        // 2) A cheaper path exists that never exceeds a diff of 1.
        int[][] g2 = {
            {1, 2, 3},
            {3, 8, 4},
            {5, 3, 5},
        };
        System.out.println(sol.MinimumEffort(grid(g2))); // expected: 1

        // 3) Single cell -> no moves -> effort 0.
        int[][] g3 = { {7} };
        System.out.println(sol.MinimumEffort(grid(g3))); // expected: 0
    }
}
