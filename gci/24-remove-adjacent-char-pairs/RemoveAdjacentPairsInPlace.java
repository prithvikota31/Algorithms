/*
 * ============================================================================
 * Problem 24 (Google L4 prep) - Follow-up: remove adjacent opposite-case
 * pairs WITHOUT an explicit stack (in-place, O(1) extra space).
 * ============================================================================
 *
 * PROMPT
 *   Same as the base problem, but solve it without allocating any separate
 *   stack structure (not even a StringBuilder used as a stack) -- reuse the
 *   input's own character array.
 *
 * EXAMPLE
 *   "abBA" -> "" (same cases as the base problem)
 *
 * INTUITION
 *   The base solution's "stack" is really just an invariant: at every step,
 *   the characters processed so far collapse down to some cleaned prefix.
 *   That prefix can live inside the input array itself -- no need for a
 *   second buffer. A single index `top` marks the boundary: everything in
 *   chars[0, top) is the cleaned string so far; everything at or past `top`
 *   is unprocessed/discarded.
 *
 * ALGORITHM
 *   Convert the string to a char[]. Walk it left to right with `top`
 *   starting at 0:
 *     - if top > 0 and chars[top-1] cancels with the current character,
 *       "pop" by doing top-- (no need to erase, it's just overwritten later).
 *     - otherwise "push" by writing chars[top] = current, then top++.
 *   The answer is the substring chars[0, top).
 *
 * COMPLEXITY
 *   Time:  O(n) -- each character is visited once.
 *   Space: O(1) extra (in-place on the character array; excludes input/output).
 * ============================================================================
 */
public class RemoveAdjacentPairsInPlace {

    public String makeGood(String s) {
        char[] chars = s.toCharArray();
        int top = 0;

        for (char current : chars) {
            if (top > 0 && isOppositeCase(chars[top - 1], current)) {
                top--;
            } else {
                chars[top] = current;
                top++;
            }
        }

        return new String(chars, 0, top);
    }

    private boolean isOppositeCase(char a, char b) {
        return Math.abs(a - b) == 32;
    }

    public static void main(String[] args) {
        RemoveAdjacentPairsInPlace sol = new RemoveAdjacentPairsInPlace();

        check("simple pair", sol.makeGood("aA"), "");
        check("cascading cancellation", sol.makeGood("abBA"), "");
        check("no cancellation possible", sol.makeGood("s"), "s");
        check("single cancellation mid-string", sol.makeGood("leEeCode"), "leCode");
        check("no adjacent pairs cancel", sol.makeGood("abc"), "abc");
        check("empty string", sol.makeGood(""), "");

        System.out.println("all passed");
    }

    private static void check(String name, String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got \"" + actual
                    + "\" want \"" + expected + "\"");
        }
        System.out.println("pass " + name + " -> \"" + actual + "\"");
    }
}
