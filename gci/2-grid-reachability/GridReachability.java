/*
 * ============================================================================
 * Problem 2 (Google L4 prep) — Grid Source-to-Target Reachability
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Grid of S (source), T (target), '.' (walkable land), 'W' (water/blocker).
 * Move up/down/left/right through non-water cells. Return whether any path
 * from S to T exists.
 *
 * Reported Google follow-up (NOT solved here): find an S->T path whose minimum
 * Manhattan distance from a cat/danger source is as LARGE as possible (a
 * maximin "safest path"). We solve only the base reachability now.
 *
 * EXAMPLE
 *   S . W .
 *   . . W .
 *   W . . T     -> true  (S down/right around the water to T)
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: grid traversal -> BFS/DFS. Each walkable cell is a node; adjacent
 * walkable cells share an edge; the question is "is T reachable from S?".
 *
 * Invariant: every cell placed in the queue is reachable from S. Mark visited
 * WHEN ENQUEUEING (not when dequeuing) so no cell is added twice.
 *
 * APPROACHES
 *   Brute force : try every path with backtracking -> O(4^(R*C)). Adding a
 *                 global visited[][] collapses it to normal BFS/DFS.
 *   Optimal     : one BFS from S (below). O(R*C) time / O(R*C) space; each
 *                 cell enqueued at most once. (DFS is equally optimal here;
 *                 BFS chosen because the safety follow-up builds on it.)
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.Queue;

public class GridReachability {

    public boolean canReachTarget(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        int sourceRow = -1;
        int sourceCol = -1;

        // Locate S. (T is detected during BFS, no need to pre-locate.)
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == 'S') {
                    sourceRow = row;
                    sourceCol = col;
                }
            }
        }
        if (sourceRow == -1) {
            return false;
        }

        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[rows][cols];

        queue.offer(new int[] {sourceRow, sourceCol});
        visited[sourceRow][sourceCol] = true;

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentCol = current[1];

            if (grid[currentRow][currentCol] == 'T') {
                return true;
            }

            for (int direction = 0; direction < 4; direction++) {
                int nextRow = currentRow + delRow[direction];
                int nextCol = currentCol + delCol[direction];

                if (nextRow >= 0 && nextRow < rows
                        && nextCol >= 0 && nextCol < cols
                        && grid[nextRow][nextCol] != 'W'
                        && !visited[nextRow][nextCol]) {
                    visited[nextRow][nextCol] = true;
                    queue.offer(new int[] {nextRow, nextCol});
                }
            }
        }

        return false;
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        GridReachability sol = new GridReachability();

        char[][] a = {
            {'S', '.', 'W', '.'},
            {'.', '.', 'W', '.'},
            {'W', '.', '.', 'T'}
        };
        System.out.println(sol.canReachTarget(a)); // true

        char[][] b = {
            {'S', 'W'},
            {'W', 'T'}
        };
        System.out.println(sol.canReachTarget(b)); // false (walled off)

        char[][] c = {
            {'S', 'T'}
        };
        System.out.println(sol.canReachTarget(c)); // true (adjacent)
    }
}
