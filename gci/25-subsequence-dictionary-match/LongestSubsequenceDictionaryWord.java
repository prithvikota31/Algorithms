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
        // ch -> list of integers for s
        Map<Character, List<Integer>> charToIndicesMap = buildMap(s);
        String bestWord = "";
        for(String word: dictionary)
        {
            if(isSubsequence(word, charToIndicesMap))
            {
                if(word.length() > bestWord.length())
                {
                    bestWord = word;
                }
                else if(word.length() == bestWord.length())
                {
                    if(word.compareTo(bestWord) < 0)
                    {
                        bestWord = word;
                    }
                }
            }
        }
        return bestWord;
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
