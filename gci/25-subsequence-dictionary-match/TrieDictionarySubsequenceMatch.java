import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * ============================================================================
 * Problem 25 (Google L4 prep) - Follow-up 2: Trie optimization when the
 * dictionary is huge and many words share prefixes.
 * ============================================================================
 *
 * PROMPT (constraint change from the base problem)
 *   Same task -- return every dictionary word that is a subsequence of s --
 *   but now the dictionary has millions of words, many sharing prefixes
 *   (e.g. "app", "apple", "applet", "apply"). Checking each word
 *   independently (the base O(D * L * log S) solution) repeats the shared
 *   prefix work D times. Instead, merge the dictionary into a Trie so the
 *   shared prefix is matched once for every word that uses it.
 *
 * EXAMPLE
 *   s = "abpcplea", dictionary = ["app", "apple", "monkey", "plea"]
 *   -> ["app", "apple", "plea"] (in that discovery order while scanning s)
 *
 * INTUITION
 *   Build a Trie over the dictionary words, then scan s once, maintaining
 *   the SET of Trie nodes reachable as "a prefix already matched by some
 *   subsequence of s so far" (currentPositions). At each character c of s,
 *   every position with a child edge c can advance one level deeper.
 *
 *   CORRECTNESS SUBTLETY (this is the part that's easy to get wrong): since
 *   we're matching a SUBSEQUENCE, not a substring, skipping a character of s
 *   must always be legal. That means every currently active position must be
 *   carried forward UNCHANGED into the next step, in addition to whichever
 *   positions advance via a matching child. Dropping a position just because
 *   the current character doesn't extend it (the naive "only keep matched
 *   children" version) silently loses any word whose next required
 *   character appears further down s -- for example scanning "abpcplea"
 *   for "apple" needs to hold the 'a' state through the 'b', 'c' that come
 *   between the two consumed 'p's.
 *
 * ALGORITHM
 *   1. Build the Trie: each edge is a character, each terminal node stores
 *      the word (if any) that ends there.
 *   2. currentPositions = {root}; if root itself is a word (empty-string
 *      dictionary entry), record it immediately -- root is never reached
 *      through the "advanced" path below, so it needs this one-time check.
 *   3. For each character c of s:
 *        nextPositions = copy of currentPositions (skip c for every position)
 *        for each position in currentPositions: if it has a child c, add
 *          that child to nextPositions; the first time a position newly
 *          enters the set, if it holds a word, record it (a Set naturally
 *          dedupes repeat arrivals).
 *        currentPositions = nextPositions
 *   4. Return the recorded words.
 *
 * COMPLEXITY
 *   Let S = |s|, T = total characters across all dictionary words.
 *   Building the Trie: O(T). Since currentPositions only grows (positions
 *   are carried forward, never dropped), it's bounded by the Trie's node
 *   count (<= T).
 *   Time:  O(T + S * T) worst case (each of S characters scans the position
 *          set); in practice the set stays far smaller than T.
 *   Space: O(T) for the Trie plus O(T) for the position set.
 * ============================================================================
 */
public class TrieDictionarySubsequenceMatch {

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        String word;
    }

    public List<String> findWords(String s, String[] dictionary) {
        TrieNode root = buildTrie(dictionary);
        List<String> result = new ArrayList<>();

        // currentPositions = every Trie node reachable via some subsequence of s read so far.
        Set<TrieNode> currentPositions = new HashSet<>();
        currentPositions.add(root);
        if (root.word != null) {
            result.add(root.word);
        }

        for (char c : s.toCharArray()) {
            // Carry every position forward unchanged (skipping c is always legal for a subsequence).
            Set<TrieNode> nextPositions = new HashSet<>(currentPositions);
            for (TrieNode position : currentPositions) {
                TrieNode advanced = position.children.get(c);

                if (advanced == null) {
                    continue; // no edge for c from this position -- nothing to advance
                }

                boolean isNewlyReached = nextPositions.add(advanced);
                if (isNewlyReached && advanced.word != null) {
                    result.add(advanced.word);
                }
            }
            currentPositions = nextPositions;
        }

        return result;
    }

    private TrieNode buildTrie(String[] dictionary) {
        TrieNode root = new TrieNode();
        for (String word : dictionary) {
            TrieNode current = root;
            for (char c : word.toCharArray()) {
                current = current.children.computeIfAbsent(c, key -> new TrieNode());
            }
            current.word = word;
        }
        return root;
    }

    public static void main(String[] args) {
        TrieDictionarySubsequenceMatch sol = new TrieDictionarySubsequenceMatch();

        check("shared-prefix dictionary, subsequence spans skipped characters",
                sol.findWords("abpcplea", new String[] { "app", "apple", "monkey", "plea" }),
                List.of("app", "apple", "plea"));

        check("order matters", sol.findWords("apple", new String[] { "apl", "pal" }),
                List.of("apl"));

        check("no matches", sol.findWords("xyz", new String[] { "abc" }),
                List.of());

        check("empty dictionary word is trivially a subsequence", sol.findWords("abc", new String[] { "" }),
                List.of(""));

        check("repeated characters needed", sol.findWords("aabb", new String[] { "ab", "abab", "aabb" }),
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
