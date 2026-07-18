/*
 * ============================================================================
 * Problem 2 follow-up (Google L4 prep) — Safest Path from S to T
 *                        (maximize the minimum distance from the cat)
 *                        [LeetCode 1102-style "Path With Maximum Minimum Value"]
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Grid with S, T, one cat C, walkable '.', water 'W'. Move 4-directionally
 * through non-water, non-cat cells. Each cell's "safety" = its Manhattan
 * distance to the cat (distance ignores walls; the mouse is blocked by them).
 * A path's score = the MINIMUM cell safety along it. Return the maximum score
 * over all S->T paths, or -1 if T is unreachable.
 *
 * ASSUMPTION: a single cat. With multiple cats, a cell's safety would be the
 * distance to the NEAREST cat (min over cats) — precompute a catDist[][] then.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: MAXIMUM-BOTTLENECK path -> max-heap Dijkstra.
 *   - cell safety      = Manhattan distance to cat
 *   - path score       = min cell safety seen so far
 *   - always expand the path with the LARGEST current score
 *       nextPathSafety = min(currentSafety, safety(next))
 *   - a max-heap yields the safest frontier first; when T is POPPED its score
 *     is globally optimal (same argument as Dijkstra, maximizing a bottleneck
 *     instead of minimizing a sum). Skip stale heap entries.
 *
 * APPROACHES
 *   Binary search + BFS on a safety threshold — valid, O(RC log(maxDist)).
 *   Max-heap Dijkstra (below) — more direct, O(RC log(RC)).
 * ----------------------------------------------------------------------------
 */

import java.util.Arrays;
import java.util.PriorityQueue;

public class MaximumSafetyPath {

    public int maximumSafety(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        int sourceRow = -1, sourceCol = -1;
        int targetRow = -1, targetCol = -1;
        int catRow = -1, catCol = -1;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char cell = grid[row][col];
                if (cell == 'S') {
                    sourceRow = row;
                    sourceCol = col;
                } else if (cell == 'T') {
                    targetRow = row;
                    targetCol = col;
                } else if (cell == 'C') {
                    catRow = row;
                    catCol = col;
                }
            }
        }

        // Defensive: need a source and a target to have any answer.
        if (sourceRow == -1 || targetRow == -1) {
            return -1;
        }

        // bestSafety[r][c] = largest bottleneck safety found to reach (r,c).
        int[][] bestSafety = new int[rows][cols];
        for (int[] row : bestSafety) {
            Arrays.fill(row, -1);
        }

        // Max-heap ordered by path safety. Entry: {pathSafety, row, col}.
        PriorityQueue<int[]> maxHeap =
                new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));

        int sourceSafety = getDistance(sourceRow, sourceCol, catRow, catCol);
        bestSafety[sourceRow][sourceCol] = sourceSafety;
        maxHeap.offer(new int[] {sourceSafety, sourceRow, sourceCol});

        int[] delRow = {0, 1, 0, -1};
        int[] delCol = {1, 0, -1, 0};

        while (!maxHeap.isEmpty()) {
            int[] current = maxHeap.poll();
            int currentSafety = current[0];
            int currentRow = current[1];
            int currentCol = current[2];

            // Stale entry — a better path to this cell already settled.
            if (currentSafety < bestSafety[currentRow][currentCol]) {
                continue;
            }

            // Highest safety remaining in the heap: T's score is now optimal.
            if (currentRow == targetRow && currentCol == targetCol) {
                return currentSafety;
            }

            for (int direction = 0; direction < 4; direction++) {
                int nextRow = currentRow + delRow[direction];
                int nextCol = currentCol + delCol[direction];

                boolean canMove = nextRow >= 0 && nextRow < rows
                        && nextCol >= 0 && nextCol < cols
                        && grid[nextRow][nextCol] != 'W'
                        && grid[nextRow][nextCol] != 'C';
                if (!canMove) {
                    continue;
                }

                int nextCellSafety = getDistance(nextRow, nextCol, catRow, catCol);
                // A path is only as safe as its least-safe cell.
                int nextPathSafety = Math.min(currentSafety, nextCellSafety);

                if (nextPathSafety > bestSafety[nextRow][nextCol]) {
                    bestSafety[nextRow][nextCol] = nextPathSafety;
                    maxHeap.offer(new int[] {nextPathSafety, nextRow, nextCol});
                }
            }
        }

        return -1;
    }

    private int getDistance(int row, int col, int catRow, int catCol) {
        return Math.abs(row - catRow) + Math.abs(col - catCol);
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        MaximumSafetyPath sol = new MaximumSafetyPath();

        // Cat dead center: any corner-to-corner path is forced through a
        // distance-1 cell -> bottleneck 1.
        char[][] a = {
            {'S', '.', '.'},
            {'.', 'C', '.'},
            {'.', '.', 'T'}
        };
        System.out.println(sol.maximumSafety(a)); // 1

        // Cat in the far corner; top row stays as far as possible. T itself is
        // distance 2 from the cat, so bottleneck is capped at 2.
        char[][] b = {
            {'S', '.', 'T'},
            {'.', '.', '.'},
            {'.', '.', 'C'}
        };
        System.out.println(sol.maximumSafety(b)); // 2

        // Source boxed in by water -> unreachable.
        char[][] c = {
            {'S', 'W', '.'},
            {'W', 'W', '.'},
            {'C', '.', 'T'}
        };
        System.out.println(sol.maximumSafety(c)); // -1
    }
}
