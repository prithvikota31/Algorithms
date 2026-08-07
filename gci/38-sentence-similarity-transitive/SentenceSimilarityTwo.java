import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================================
 * Problem 38 (Google L4 prep) — Sentence Similarity II (transitive)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Two sentences (arrays of words) are "similar" when they have the SAME LENGTH
 * and every aligned pair sentence1[i] / sentence2[i] is either the identical
 * word or connected through `similarPairs`. Unlike Sentence Similarity I, the
 * relation is TRANSITIVE: great~good and fine~good implies great~fine.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   Is similarity reflexive for words that never appear in `similarPairs`?
 *   Yes — "cat" matches "cat" even with an empty pair list. That is why the
 *   equality check happens BEFORE consulting the union-find.
 *
 * EXAMPLES
 *   ["great","acting","skills"] vs ["fine","drama","talent"]
 *   pairs = [[great,good],[fine,good],[drama,acting],[skills,talent]]
 *     great—good—fine is one component -> true
 *
 *   ["great","acting","skills"] vs ["fine","painting","talent"], same pairs
 *     "painting" appears in no pair -> false
 *
 *   ["great"] vs ["fine","good"]            -> false (length mismatch)
 *   ["cat"]   vs ["cat"], pairs = []        -> true  (reflexive)
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Story cue: "things that share a property TRANSITIVELY" => UNION-FIND.
 *
 * The pairs are edges of an undirected graph over words. Transitive closure of
 * "similar" is exactly "in the same connected component". So the whole problem
 * collapses to: union every pair, then ask "same root?" for each aligned word.
 *
 *   parent[word] = the word one step up toward its component representative
 *   size[root]   = number of words in that component
 *
 * UNION BY SIZE (this file's variant):
 *   size[root] = number of nodes in that component; the SMALLER tree goes
 *   under the BIGGER tree.
 *
 * Why that keeps trees shallow: a node's depth only increases when the tree it
 * lives in is the smaller one being absorbed — and that means the component it
 * belongs to at least DOUBLED. A node can double at most log(n) times, so no
 * node can ever sink deeper than log(n) levels. Attaching the bigger tree under
 * the smaller one instead would deepen the many nodes to shorten the few.
 *
 * Union by size vs union by rank: rank tracks an upper bound on tree HEIGHT,
 * size tracks node COUNT. Both give the same O(log n) depth bound and the same
 * near-constant amortized cost with path compression; size is preferred when
 * you also want the component's population for free (a very common follow-up:
 * "how many words are similar to X?" is just size.get(find(X))).
 *
 * PATH COMPRESSION: `find` re-points every node it walks past straight at the
 * root, so the second query on the same chain is flat.
 *
 * APPROACHES
 *   Brute force : build an adjacency list and run BFS/DFS from sentence1[i] to
 *                 see if it reaches sentence2[i]. O(n * (V + E)) — a fresh
 *                 traversal per word position.
 *   Optimal     : union-find (below). Components are built once, then each
 *                 comparison is a near-O(1) root lookup.
 *
 * COMPLEXITY
 *   Let P = similarPairs.size(), N = sentence length, W = distinct words.
 *   Time  O((P + N) * alpha(W)) ~ O(P + N)   (alpha = inverse Ackermann)
 *   Space O(W) for the parent and size maps
 * ----------------------------------------------------------------------------
 */
public class SentenceSimilarityTwo {

    public boolean areSentencesSimilarTwo(String[] sentence1, String[] sentence2,
            List<List<String>> similarPairs) {
        if (sentence1.length != sentence2.length) {
            return false;
        }

        DisjointSet similarity = new DisjointSet();
        for (List<String> pair : similarPairs) {
            similarity.union(pair.get(0), pair.get(1));
        }

        for (int i = 0; i < sentence1.length; i++) {
            String word1 = sentence1[i];
            String word2 = sentence2[i];

            // Reflexivity: identical words match even if no pair ever named them.
            if (word1.equals(word2)) {
                continue;
            }

            // A word absent from every pair is alone in its own component.
            if (!similarity.contains(word1) || !similarity.contains(word2)) {
                return false;
            }

            if (!similarity.find(word1).equals(similarity.find(word2))) {
                return false;
            }
        }

        return true;
    }

    /** Union-find over word names: UNION BY SIZE + path compression. */
    private static final class DisjointSet {

        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> size = new HashMap<>();

        boolean contains(String word) {
            return parent.containsKey(word);
        }

        /** Number of words similar to `word`, including itself. */
        int componentSize(String word) {
            return size.get(find(word));
        }

        void union(String word1, String word2) {
            add(word1);
            add(word2);

            String root1 = find(word1);
            String root2 = find(word2);
            if (root1.equals(root2)) {
                return;
            }

            // Make root1 the larger component, then hang the smaller one under it.
            if (size.get(root1) < size.get(root2)) {
                String smaller = root1;
                root1 = root2;
                root2 = smaller;
            }

            parent.put(root2, root1);
            size.put(root1, size.get(root1) + size.get(root2));
        }

        String find(String word) {
            String next = parent.get(word);
            if (next.equals(word)) {
                return word;
            }

            String root = find(next);
            parent.put(word, root); // Path compression.
            return root;
        }

        /** A brand-new word starts as its own root, in a component of size 1. */
        private void add(String word) {
            parent.putIfAbsent(word, word);
            size.putIfAbsent(word, 1);
        }
    }

    public static void main(String[] args) {
        SentenceSimilarityTwo solution = new SentenceSimilarityTwo();

        List<List<String>> pairs = List.of(
                List.of("great", "good"),
                List.of("fine", "good"),
                List.of("drama", "acting"),
                List.of("skills", "talent"));

        // great—good—fine is one component, so great ~ fine transitively.
        check("transitive match through a shared neighbour",
                solution.areSentencesSimilarTwo(
                        new String[] { "great", "acting", "skills" },
                        new String[] { "fine", "drama", "talent" },
                        pairs),
                true);

        // "painting" appears in no pair -> its component is just itself.
        check("word missing from all pairs",
                solution.areSentencesSimilarTwo(
                        new String[] { "great", "acting", "skills" },
                        new String[] { "fine", "painting", "talent" },
                        pairs),
                false);

        check("length mismatch short-circuits",
                solution.areSentencesSimilarTwo(
                        new String[] { "great" },
                        new String[] { "fine", "good" },
                        pairs),
                false);

        check("identical words are similar with no pairs at all",
                solution.areSentencesSimilarTwo(
                        new String[] { "cat", "dog" },
                        new String[] { "cat", "dog" },
                        List.of()),
                true);

        check("two empty sentences",
                solution.areSentencesSimilarTwo(new String[] {}, new String[] {}, List.of()),
                true);

        // Both known words, but they live in different components.
        check("known words in different components",
                solution.areSentencesSimilarTwo(
                        new String[] { "great" },
                        new String[] { "talent" },
                        pairs),
                false);

        // Follow-up freebie that union BY SIZE gives away for free.
        DisjointSet set = new DisjointSet();
        for (List<String> pair : pairs) {
            set.union(pair.get(0), pair.get(1));
        }
        if (set.componentSize("great") != 3) {
            throw new AssertionError("FAIL componentSize: got " + set.componentSize("great"));
        }
        System.out.println("pass componentSize(great) -> 3 {great, good, fine}");

        System.out.println("all passed");
    }

    private static void check(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
