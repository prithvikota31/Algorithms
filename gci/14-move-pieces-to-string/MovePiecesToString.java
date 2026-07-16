/*
 * ============================================================================
 * Problem 14 (Google L4 prep) — Move Pieces to Obtain a String  [LeetCode 2337]
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Strings of 'L', 'R', '_'. Allowed moves:
 *      _L -> L_   (an 'L' may move LEFT into an adjacent blank)
 *      R_ -> _R   (an 'R' may move RIGHT into an adjacent blank)
 * Can `start` be transformed into `target`?  ('_' are blanks, not pieces.)
 *
 * EXAMPLE
 *   start = "_L_R_", target = "L___R"  ->  true
 *     _L_R_ -> L__R_ -> L___R
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: TWO POINTERS + order-preserving matching. Ignore blanks and line up
 * each real piece in `start` with the corresponding real piece in `target`.
 * Pieces CANNOT cross, so the sequences of non-'_' chars must be identical, and:
 *   - 'L' may only move left  -> its start index must be >= its target index.
 *   - 'R' may only move right -> its start index must be <= its target index.
 *
 * Memory sentence: same piece order; L can't end farther RIGHT, R can't end
 * farther LEFT.
 *
 * APPROACHES
 *   Brute force : BFS over every reachable string (apply each legal move, visited
 *                 set). Exponential — O(n * 3^n) time / O(3^n) space. Unusable.
 *   Optimal     : one linear pass with two pointers (below).
 *
 * ALGORITHM
 *   startIndex scans start, targetIndex scans target.
 *   1. Skip '_' in both.
 *   2. If one ran out -> valid only if BOTH ran out (equal piece counts).
 *   3. Pieces must match (same relative order).
 *   4. 'L' && startIndex < targetIndex  -> would move right -> false.
 *      'R' && startIndex > targetIndex  -> would move left  -> false.
 *   5. Advance both.
 *
 * COMPLEXITY
 *   Time O(n)   Space O(1)   (n = start.length())
 * ----------------------------------------------------------------------------
 */

public class MovePiecesToString {

    public boolean canChange(String start, String target) {
        int n = start.length();
        int startIndex = 0;
        int targetIndex = 0;

        while (startIndex < n || targetIndex < n) {

            // Blank spaces do not represent pieces, so skip them.
            while (startIndex < n && start.charAt(startIndex) == '_') {
                startIndex++;
            }
            while (targetIndex < n && target.charAt(targetIndex) == '_') {
                targetIndex++;
            }

            // One string has remaining pieces while the other does not.
            if (startIndex == n || targetIndex == n) {
                return startIndex == n && targetIndex == n;
            }

            char piece = start.charAt(startIndex);

            // Corresponding pieces must appear in the same relative order.
            if (piece != target.charAt(targetIndex)) {
                return false;
            }

            // L cannot move right.
            if (piece == 'L' && startIndex < targetIndex) {
                return false;
            }

            // R cannot move left.
            if (piece == 'R' && startIndex > targetIndex) {
                return false;
            }

            startIndex++;
            targetIndex++;
        }

        return true;
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        MovePiecesToString sol = new MovePiecesToString();

        System.out.println(sol.canChange("_L_R_", "L___R")); // true
        System.out.println(sol.canChange("R_L_", "__LR"));   // false (R can't pass L / move left)
        System.out.println(sol.canChange("_L__R__R_", "L______RR")); // true
        System.out.println(sol.canChange("L", "R"));         // false (different piece order)
    }
}
