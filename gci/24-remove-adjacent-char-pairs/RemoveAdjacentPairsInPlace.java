/*
 * ============================================================================
 * Problem 24 (Google L4 prep) - Follow-up: remove adjacent opposite-case
 * pairs WITHOUT an explicit stack (write pointer over a mutable buffer).
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
 *   Space: O(1) working space after the char[] exists. With this String API,
 *          toCharArray() and the returned String still allocate O(n) space.
 *          A char[] input plus a returned length would be truly in-place.
 * ============================================================================
 */
public class RemoveAdjacentPairsInPlace {

    public String makeGood(String s) {
        char[] chars = s.toCharArray();
        int top = 0;
        //So think of top as next free index, not the index of the last valid character.
        //aAbcCdeFf
        for(int i = 0; i < chars.length; i++)
        {
            char ch = chars[i];
            if(top > 0 && isOppositeCase(ch, chars[top - 1]))
            {
                top--;
            }
            else
            {
                chars[top] = ch;
                top++;
            }
        }

        return new String(chars, 0, top);
    }

    private boolean isOppositeCase(char a, char b) {
        return Math.abs(a - b) == Math.abs('A' - 'a');
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
