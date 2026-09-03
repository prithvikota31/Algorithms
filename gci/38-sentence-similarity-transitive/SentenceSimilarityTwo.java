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

    Map<String, String> parent = new HashMap<>();
    Map<String, Integer> size = new HashMap<>();
    public boolean areSentencesSimilarTwo(
            String[] sentence1,
            String[] sentence2,
            List<List<String>> similarPairs) {

        parent.clear();
        size.clear();

        if(sentence1.length != sentence2.length)
        {
            return false;
        }

        for(List<String> pair: similarPairs)
        {
            union(pair.get(0), pair.get(1));
        }

        for(int i = 0; i < sentence1.length; i++)
        {
            String s1 = sentence1[i];
            String s2 = sentence2[i];
            if(s1.equals(s2))
            {
                continue;
            }
            if(!parent.containsKey(s1) || !parent.containsKey(s2))
            {
                return false;
            }
            if(!find(s1).equals(find(s2)))
            {
                return false;
            }
        }

        return true;
    }


    private void union(String root1, String root2)
    {
        parent.putIfAbsent(root1, root1);
        parent.putIfAbsent(root2, root2);

        size.putIfAbsent(root1, 1);
        size.putIfAbsent(root2, 1);
        String s1 = find(root1);
        String s2 = find(root2);

        if(s1.equals(s2))
        {
            return;
        }

        if(size.get(s1) < size.get(s2))
        {
            parent.put(s1, s2);
            size.put(s2, size.get(s1) + size.get(s2));
        }
        else
        {
            parent.put(s2, s1);
            size.put(s1, size.get(s1) + size.get(s2));
        }
    }

    private String find(String s)
    {
        if(parent.get(s).equals(s))
        {
            return s;
        }

        parent.put(s, find(parent.get(s)));
        return parent.get(s);
    }


    public static void main(String[] args) {

        List<List<String>> pairs = List.of(
                List.of("great", "good"),
                List.of("fine", "good"),
                List.of("drama", "acting"),
                List.of("skills", "talent"));

        // great—good—fine is one component, so great ~ fine transitively.
        check("transitive match through a shared neighbour",
                new SentenceSimilarityTwo().areSentencesSimilarTwo(
                        new String[] { "great", "acting", "skills" },
                        new String[] { "fine", "drama", "talent" },
                        pairs),
                true);

        // "painting" appears in no pair -> its component is just itself.
        check("word missing from all pairs",
                new SentenceSimilarityTwo().areSentencesSimilarTwo(
                        new String[] { "great", "acting", "skills" },
                        new String[] { "fine", "painting", "talent" },
                        pairs),
                false);

        check("length mismatch short-circuits",
                new SentenceSimilarityTwo().areSentencesSimilarTwo(
                        new String[] { "great" },
                        new String[] { "fine", "good" },
                        pairs),
                false);

        check("identical words are similar with no pairs at all",
                new SentenceSimilarityTwo().areSentencesSimilarTwo(
                        new String[] { "cat", "dog" },
                        new String[] { "cat", "dog" },
                        List.of()),
                true);

        check("two empty sentences",
                new SentenceSimilarityTwo().areSentencesSimilarTwo(
                        new String[] {}, new String[] {}, List.of()),
                true);

        // Both known words, but they live in different components.
        check("known words in different components",
                new SentenceSimilarityTwo().areSentencesSimilarTwo(
                        new String[] { "great" },
                        new String[] { "talent" },
                        pairs),
                false);

        // Follow-up freebie that union BY SIZE gives away: component population.
        SentenceSimilarityTwo counter = new SentenceSimilarityTwo();
        for (List<String> pair : pairs) {
            counter.union(pair.get(0), pair.get(1));
        }
        int greatComponent = counter.size.get(counter.find("great"));
        if (greatComponent != 3) {
            throw new AssertionError("FAIL componentSize: got " + greatComponent);
        }
        System.out.println("pass size[find(great)] -> 3 {great, good, fine}");

        System.out.println("all passed");
    }

    private static void check(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
