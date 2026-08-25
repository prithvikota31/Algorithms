/*
 * ============================================================================
 * Problem 2 follow-up (Google L4 prep) — Safest Path from S to T
 *                        (maximize the minimum distance from the cat)
 *                        [LeetCode 1102-style "Path With Maximum Minimum Value"]
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Grid with S, T, one or more cats C, walkable '.', and water 'W'. Move
 * 4-directionally through non-water, non-cat cells. Each cell's "safety" is
 * its distance to the NEAREST cat. Cat distance ignores walls; only the mouse
 * is blocked by them. A path's score is the MINIMUM cell safety along it.
 * Return the maximum score over all S->T paths, or -1 if T is unreachable.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Phase 1: MULTI-SOURCE BFS, like Rotten Oranges. Put every cat in the queue at
 * distance zero. The first time BFS reaches a cell is its distance to the
 * nearest cat. Water does not block this BFS because cat distance is Manhattan
 * distance; water blocks only the mouse's path.
 *
 * Phase 2: MAXIMUM-BOTTLENECK path -> max-heap Dijkstra.
 *   - cell safety      = distance to nearest cat
 *   - path score       = min cell safety seen so far
 *   - always expand the path with the LARGEST current score
 *       nextPathSafety = min(currentSafety, safety(next))
 *   - a max-heap yields the safest frontier first; when T is POPPED its score
 *     is globally optimal (same argument as Dijkstra, maximizing a bottleneck
 *     instead of minimizing a sum). Skip stale heap entries.
 *
 * APPROACHES
 *   Binary search + BFS on a safety threshold — valid, O(RC log(maxDist)).
 *   Multi-source BFS + max-heap Dijkstra (below) — O(RC log(RC)).
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class MaximumSafetyPath {

    public int maximumSafety(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        int sourceRow = -1;
        int sourceCol = -1;
        int targetRow = -1;
        int targetCol = -1;

        int[][] safety = new int[n][m];
        for (int[] row : safety) {
            Arrays.fill(row, -1);
        }

        Queue<int[]> queue = new ArrayDeque<>();

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < m; col++) {
                if (grid[row][col] == 'S') {
                    sourceRow = row;
                    sourceCol = col;
                } else if (grid[row][col] == 'T') {
                    targetRow = row;
                    targetCol = col;
                } else if (grid[row][col] == 'C') {
                    safety[row][col] = 0;
                    queue.offer(new int[] {row, col});
                }
            }
        }

        if (sourceRow == -1 || targetRow == -1 || queue.isEmpty()) {
            return -1;
        }

        int[] delRow = {-1, 0, 1, 0};
        int[] delCol = {0, 1, 0, -1};

        // Multi-source BFS: safety[row][col] is distance to the nearest cat.
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            for (int direction = 0; direction < 4; direction++) {
                int nextRow = row + delRow[direction];
                int nextCol = col + delCol[direction];

                if (nextRow >= 0 && nextRow < n
                        && nextCol >= 0 && nextCol < m
                        && safety[nextRow][nextCol] == -1) {
                    safety[nextRow][nextCol] = safety[row][col] + 1;
                    queue.offer(new int[] {nextRow, nextCol});
                }
            }
        }

        // bestSafety[row][col] is the largest bottleneck found to reach a cell.
        int[][] bestSafety = new int[n][m];
        for (int[] row : bestSafety) {
            Arrays.fill(row, -1);
        }

        PriorityQueue<int[]> maxHeap =
                new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));

        bestSafety[sourceRow][sourceCol] = safety[sourceRow][sourceCol];
        maxHeap.offer(new int[] {
            safety[sourceRow][sourceCol], sourceRow, sourceCol
        });

        while (!maxHeap.isEmpty()) {
            int[] current = maxHeap.poll();
            int currentSafety = current[0];
            int currentRow = current[1];
            int currentCol = current[2];

            if (currentSafety < bestSafety[currentRow][currentCol]) {
                continue;
            }

            if (currentRow == targetRow && currentCol == targetCol) {
                return currentSafety;
            }

            for (int direction = 0; direction < 4; direction++) {
                int nextRow = currentRow + delRow[direction];
                int nextCol = currentCol + delCol[direction];

                boolean canMove = nextRow >= 0 && nextRow < n
                    && nextCol >= 0 && nextCol < m
                        && grid[nextRow][nextCol] != 'W'
                        && grid[nextRow][nextCol] != 'C';
                if (!canMove) {
                    continue;
                }

                int nextPathSafety = Math.min(
                    currentSafety,
                    safety[nextRow][nextCol]);

                if (nextPathSafety > bestSafety[nextRow][nextCol]) {
                    bestSafety[nextRow][nextCol] = nextPathSafety;
                    maxHeap.offer(new int[] {nextPathSafety, nextRow, nextCol});
                }
            }
        }

        return -1;
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

        // Two cats: safety is measured from the nearest one.
        char[][] d = {
            {'S', '.', '.', 'C'},
            {'.', 'W', '.', '.'},
            {'.', '.', 'W', '.'},
            {'C', '.', '.', 'T'}
        };
        System.out.println(sol.maximumSafety(d)); // 1
    }
}
