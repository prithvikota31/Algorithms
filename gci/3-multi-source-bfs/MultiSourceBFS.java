/*
 * ============================================================================
 * Problem 3 (Google L4 prep) — Multi-Source BFS: Distance to Nearest Source
 *                                                 [LeetCode 542 "01 Matrix"]
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given an m x n binary grid where 0 = source/special object and 1 = normal
 * cell, moving 4-directionally, return the distance from every cell to its
 * nearest 0.
 *
 * Same pattern covers: nearest gate, hospital, boundary, water cell, police
 * station, corrupted server, etc.
 *
 * EXAMPLE
 *   0 1 1        0 1 2
 *   1 1 1   ->   1 2 1
 *   1 1 0        2 1 0
 *   Center (1,1) is 2 moves from the nearest source.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: nearest distance from ANY of many sources, equal-cost edges
 *          -> MULTI-SOURCE BFS.
 *
 * The naive direction ("from each 1, search outward for a 0") is backwards.
 * Flip it: seed the queue with ALL zeros at distance 0 and expand every source
 * simultaneously. Picture all sources releasing water at once — the first wave
 * to reach a cell came from its nearest source.
 *
 * Invariant: when a cell is first discovered, its recorded distance IS the
 * minimum over all sources — because BFS pops cells in nondecreasing distance
 * order (0, then 1, then 2, ...).
 *
 * APPROACHES
 *   Brute force : separate BFS from every 1-cell until it hits a 0.
 *                 O((m*n)^2) time. Neighbors re-explore the same grid.
 *   Optimal     : one BFS seeded with all sources (below).
 *                 O(m*n) time / O(m*n) space; each cell is enqueued once.
 *
 * DRY RUN (input above)
 *   init  dist = [[0,-1,-1],[-1,-1,-1],[-1,-1,0]], queue = [(0,0),(2,2)]
 *   wave1 (0,1)=1 (1,0)=1 (1,2)=1 (2,1)=1
 *   wave2 (0,2)=2 (1,1)=2 (2,0)=2
 *   -> [[0,1,2],[1,2,1],[2,1,0]]
 *
 * Memory line: many destinations hunting the nearest source -> reverse it and
 * BFS out from every source at once.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class MultiSourceBFS {

    public int[][] updateMatrix(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        int[][] distance = new int[rows][cols];
        Queue<int[]> queue = new ArrayDeque<>();

        // Seed every source (0) at distance 0; mark others unvisited with -1.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (mat[row][col] == 0) {
                    distance[row][col] = 0;
                    queue.offer(new int[] {row, col});
                } else {
                    distance[row][col] = -1;
                }
            }
        }

        int[][] directions = {
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0}
        };

        // Sources expand simultaneously. First arrival = shortest distance.
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentCol = current[1];

            for (int[] direction : directions) {
                int nextRow = currentRow + direction[0];
                int nextCol = currentCol + direction[1];

                if (nextRow < 0 || nextRow >= rows
                        || nextCol < 0 || nextCol >= cols) {
                    continue;
                }

                // Already has its shortest distance.
                if (distance[nextRow][nextCol] != -1) {
                    continue;
                }

                distance[nextRow][nextCol] = distance[currentRow][currentCol] + 1;
                queue.offer(new int[] {nextRow, nextCol});
            }
        }

        return distance;
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        MultiSourceBFS sol = new MultiSourceBFS();

        int[][] a = {
            {0, 1, 1},
            {1, 1, 1},
            {1, 1, 0}
        };
        System.out.println(Arrays.deepToString(sol.updateMatrix(a)));
        // [[0, 1, 2], [1, 2, 1], [2, 1, 0]]

        int[][] b = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        System.out.println(Arrays.deepToString(sol.updateMatrix(b)));
        // [[0, 0, 0], [0, 1, 0], [0, 0, 0]]

        int[][] c = {
            {0, 1, 1, 1, 1}
        };
        System.out.println(Arrays.deepToString(sol.updateMatrix(c)));
        // [[0, 1, 2, 3, 4]]
    }
}
