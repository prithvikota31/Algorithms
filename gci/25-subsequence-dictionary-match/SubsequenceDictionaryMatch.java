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

    public List<String> findWords(String source, String[] dictionary) {
        Map<Character, List<Integer>> charToIndices = buildMap(source);
        List<String> matchingWords = new ArrayList<>();

        for(String word: dictionary)
        {
            if(isSubsequence(word, charToIndices))
            {
                matchingWords.add(word);
            }
        }
        return matchingWords;
    }

    private boolean isSubsequence(String word, Map<Character, List<Integer>> charToIndices)
    {
        // Source index used to match the previous character of the word.
        int previousMatchedIndex = -1;

        for(int i = 0; i < word.length(); i++)
        {
            char currentChar = word.charAt(i);
            // All source indices at which the current character occurs.
            List<Integer> occurrenceIndices = charToIndices.get(currentChar);
            if(occurrenceIndices == null)
            {
                return false;
            }

                // Find the earliest occurrence after the previous match.
            int nextMatchedIndex = findIndiceBinarySearch(
                    occurrenceIndices, previousMatchedIndex);

            if(nextMatchedIndex == -1)
            {
                return false;
            }
            previousMatchedIndex = nextMatchedIndex;

        }
        return true;

    }

    private int findIndice(List<Integer> occurrenceIndices, int previousMatchedIndex)
    {
        // The next source index must be after the previous matched index.
        for(int occurrenceIndex: occurrenceIndices)
        {
            if(occurrenceIndex > previousMatchedIndex)
            {
                return occurrenceIndex;
            }
        }
        return -1;
    }

    private int findIndiceBinarySearch(
            List<Integer> occurrenceIndices, int previousMatchedIndex)
    {
        int low = 0;
        int high = occurrenceIndices.size() - 1;
        int nextMatchedIndex = -1;
        while(low <= high)
        {
            int mid = low + (high - low) / 2;
            if(occurrenceIndices.get(mid) > previousMatchedIndex)
            {
                nextMatchedIndex = occurrenceIndices.get(mid);
                high = mid - 1;
            }
            else
            {
                low = mid + 1;
            }
        }
        return nextMatchedIndex;
    }

    private Map<Character, List<Integer>> buildMap(String source)
    {
        Map<Character, List<Integer>> charToIndices = new HashMap<>();
        // character -> sorted list of source indices
        //apple
        //a - 0
        //p - 1, 2
        //l - 3
        //e - 4
        for(int i = 0; i < source.length(); i++)
        {
            char currentChar = source.charAt(i);
            charToIndices.putIfAbsent(currentChar, new ArrayList<>());
            charToIndices.get(currentChar).add(i);
        }
        return charToIndices;
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
