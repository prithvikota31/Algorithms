import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================================
 * Problem 25 (Google L4 prep) - Dictionary words as subsequences of a string.
 * ============================================================================
 *
 * PROMPT
 *   Given a string s and a dictionary of words, return every dictionary word
 *   that is a subsequence of s (characters appear in the same relative order,
 *   not necessarily adjacent).
 *
 * AMBIGUITY
 *   "Find a match" could mean: does at least one word qualify, return all
 *   qualifying words, or return only the longest one. This solution returns
 *   ALL qualifying words (the most general version, from which "any" or
 *   "longest" are trivial follow-ups).
 *
 * EXAMPLE
 *   s = "abpcplea", dictionary = ["ale", "apple", "monkey", "plea"]
 *   -> ["ale", "apple", "plea"]  ("monkey" fails: 'm' never appears in s)
 *   "apl" is a subsequence of "apple"; "pal" is NOT (wrong order).
 *
 * INTUITION
 *   Brute force checks each dictionary word by scanning all of s: O(D * S).
 *   Since s is fixed and queried many times (once per dictionary word),
 *   preprocess it instead: for every character, record the sorted list of
 *   indices where it occurs in s. To check if a word is a subsequence, walk
 *   its characters and, for each one, binary-search for the smallest index
 *   in that character's list which is strictly greater than the last index
 *   used -- this is the earliest s can "supply" that character next.
 *
 * ALGORITHM
 *   1. Build a Map<Character, List<Integer>> of occurrence indices in s
 *      (each list is naturally sorted ascending, built by a single scan).
 *   2. For each dictionary word, maintain currentIndex = -1.
 *      For each character c in the word:
 *        - look up positions.get(c); if absent, the word fails.
 *        - binary-search that list for the smallest value > currentIndex.
 *        - if none exists, the word fails; otherwise advance currentIndex.
 *      The word qualifies iff every character was matched.
 *
 * COMPLEXITY
 *   Let S = |s|, D = number of dictionary words, L = average word length.
 *   Time:  O(S) to build the index, O(D * L * log S) to check all words.
 *   Space: O(S) for the index.
 * ============================================================================
 */
public class SubsequenceDictionaryMatch {

    public List<String> findWords(String s, String[] dictionary) {
        Map<Character, List<Integer>> positions = buildIndex(s);

        List<String> result = new ArrayList<>();
        for (String word : dictionary) {
            if (isSubsequence(word, positions)) {
                result.add(word);
            }
        }
        return result;
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
        SubsequenceDictionaryMatch sol = new SubsequenceDictionaryMatch();

        check("classic example", sol.findWords("abpcplea",
                new String[] { "ale", "apple", "monkey", "plea" }),
                List.of("ale", "apple", "plea"));

        check("order matters", sol.findWords("apple",
                new String[] { "apl", "pal" }),
                List.of("apl"));

        check("no matches", sol.findWords("xyz",
                new String[] { "abc" }),
                List.of());

        check("empty word is trivially a subsequence", sol.findWords("abc",
                new String[] { "" }),
                List.of(""));

        check("repeated characters needed", sol.findWords("aabb",
                new String[] { "ab", "abab", "aabb" }),
                List.of("ab", "aabb"));

        System.out.println("all passed");
    }

    private static void check(String name, List<String> actual, List<String> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
