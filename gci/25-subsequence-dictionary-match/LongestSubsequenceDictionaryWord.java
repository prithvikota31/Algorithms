import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================================
 * Problem 25 (Google L4 prep) - Follow-up 1: longest dictionary word that is
 * a subsequence of s, tie-broken lexicographically.
 * ============================================================================
 *
 * PROMPT (changed from the base problem)
 *   Instead of returning ALL qualifying dictionary words, return only the
 *   longest one. If multiple words tie for longest, return the
 *   lexicographically smallest of those.
 *
 * EXAMPLE
 *   s = "abpcplea", dictionary = ["ale", "apple", "monkey", "plea"]
 *   -> "apple" (length 5; "monkey" isn't even a subsequence)
 *
 *   Tie example: s = "abpcplea", dictionary = ["ale", "abe"]
 *   Both are valid subsequences of length 3 ('a' at 0, then 'l'/'b', then
 *   'e' at 6) -- "abe" wins because it's lexicographically smaller.
 *
 * INTUITION
 *   The subsequence check itself is unchanged from the base problem (index
 *   s by character, then binary-search the next usable position for each
 *   character of a word). Only the reduction step changes: instead of
 *   collecting every valid word, keep a single running "best" candidate and
 *   replace it whenever a longer (or tied-but-smaller) word is found. No DP
 *   is needed -- each dictionary word is validated independently, so this is
 *   still just "check + greedy compare-and-replace".
 *
 * ALGORITHM
 *   1. Build the same Map<Character, List<Integer>> index of s.
 *   2. bestWord = "" (also correctly doubles as the "no match" result).
 *   3. For each dictionary word that is a subsequence of s:
 *        - replace bestWord if word.length() > bestWord.length(), OR
 *        - replace bestWord if lengths tie and word is lexicographically
 *          smaller (word.compareTo(bestWord) < 0).
 *
 * COMPLEXITY
 *   Let S = |s|, D = number of dictionary words, L = average word length.
 *   Time:  O(S) to build the index, O(D * L * log S) to check all words.
 *   Space: O(S) for the index.
 * ============================================================================
 */
public class LongestSubsequenceDictionaryWord {

    public String findLongestWord(String s, String[] dictionary) {
        Map<Character, List<Integer>> positions = buildIndex(s);

        String bestWord = "";
        for (String word : dictionary) {
            if (!isSubsequence(word, positions)) {
                continue;
            }
            if (word.length() > bestWord.length()
                    || (word.length() == bestWord.length() && word.compareTo(bestWord) < 0)) {
                bestWord = word;
            }
        }
        return bestWord;
    }

    private Map<Character, List<Integer>> buildIndex(String s) {
        Map<Character, List<Integer>> positions = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            positions.computeIfAbsent(c, key -> new ArrayList<>()).add(i);
        }
        return positions;
    }

    private boolean isSubsequence(String word, Map<Character, List<Integer>> positions) {
        int currentIndex = -1;

        for (char c : word.toCharArray()) {
            List<Integer> list = positions.get(c);
            if (list == null) {
                return false;
            }

            int nextPosition = binarySearchNext(list, currentIndex);
            if (nextPosition == -1) {
                return false;
            }

            currentIndex = nextPosition;
        }

        return true;
    }

    // Returns the smallest value in positions strictly greater than target, or -1 if none.
    private int binarySearchNext(List<Integer> positions, int target) {
        int left = 0;
        int right = positions.size() - 1;
        int answer = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (positions.get(mid) > target) {
                answer = positions.get(mid);
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return answer;
    }

    public static void main(String[] args) {
        LongestSubsequenceDictionaryWord sol = new LongestSubsequenceDictionaryWord();

        check("classic example", sol.findLongestWord("abpcplea",
                new String[] { "ale", "apple", "monkey", "plea" }),
                "apple");

        check("tie broken lexicographically", sol.findLongestWord("abpcplea",
                new String[] { "ale", "abe" }),
                "abe");

        check("no matches returns empty string", sol.findLongestWord("xyz",
                new String[] { "abc" }),
                "");

        check("empty dictionary word is trivially a subsequence", sol.findLongestWord("abc",
                new String[] { "" }),
                "");

        check("longer non-tied word wins outright", sol.findLongestWord("abpcplea",
                new String[] { "plea", "apple" }),
                "apple");

        System.out.println("all passed");
    }

    private static void check(String name, String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> \"" + actual + "\"");
    }
}
