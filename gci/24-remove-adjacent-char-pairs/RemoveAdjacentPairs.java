/*
 * ============================================================================
 * Problem 24 (Google L4 prep) - Remove adjacent same-letter opposite-case pairs.
 * ============================================================================
 *
 * PROMPT
 *   Given a string, repeatedly remove adjacent characters that are the same
 *   letter but opposite case (e.g. 'a' and 'A'). Return the final string.
 *   Same pattern as LeetCode 1544 "Make The String Great".
 *
 * EXAMPLE
 *   "abBA"    -> "abBA" -> remove "bB" -> "aA" -> remove "aA" -> ""
 *   "leEeCode" -> remove "eE" -> "leCode" (no further cancellations)
 *   "s"        -> "s" (no pair to cancel)
 *
 * INTUITION
 *   A cancellation can only ever happen between the current character and
 *   the *last surviving* character before it -- everything earlier is
 *   irrelevant once that surviving character is settled. That "last
 *   survivor" access pattern is exactly a stack. Removing a pair can also
 *   expose a brand new adjacent pair (the characters on either side of the
 *   removed pair become neighbors), which the stack handles for free since
 *   popping naturally re-exposes the previous survivor for comparison.
 *
 * ALGORITHM
 *   Use the output StringBuilder itself as the stack (no separate Deque
 *   needed):
 *     For each character c in the string:
 *       - if the stack is non-empty and its top cancels with c
 *         (opposite case of the same letter -- ASCII difference is exactly
 *         32), pop the top (delete last char).
 *       - otherwise push c (append).
 *   The final stack contents are the answer.
 *
 * COMPLEXITY
 *   Time:  O(n) -- each character is pushed once and popped at most once.
 *   Space: O(n) for the stack/output.
 * ============================================================================
 */
public class RemoveAdjacentPairs {

    public String makeGood(String s) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < s.length(); i++)
        {
            char curChar = s.charAt(i);
            if(sb.length() != 0)
            {
                char lastChar = sb.charAt(sb.length() - 1);
                if(isOppositeCase(curChar, lastChar))
                {
                    sb.deleteCharAt(sb.length() - 1);
                    continue;
                }
            }
            sb.append(curChar);

        }
        return sb.toString();

    }

    private boolean isOppositeCase(char c1, char c2)
    {
        return Math.abs('a' - 'A') == Math.abs(c1 - c2);
    }



    public static void main(String[] args) {
        RemoveAdjacentPairs sol = new RemoveAdjacentPairs();

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
