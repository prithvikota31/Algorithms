import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/*
 * ============================================================================
 * Problem 40 (Google L4 prep) — Token Translator (transitive mappings)
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * You are given translation mappings between language tokens, e.g.
 * hello -> hola, hola -> bonjour, bonjour -> ciao. A token may have no DIRECT
 * mapping to the target, but translations chain: can `source` be translated
 * into `target` by following known mappings?
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   Are mappings DIRECTED or symmetric? This file treats them as directed
 *   (a -> b does not imply b -> a), which is the stricter reading. If the
 *   interviewer says translation is bidirectional, add the reverse edge when
 *   building the graph — nothing else changes. Mapping cycles must not hang
 *   the search either way, which `visited` already guarantees.
 *
 * EXAMPLES
 *   mappings = [[cat,gato],[gato,chat],[dog,perro]]
 *     cat -> chat   : cat->gato->chat            -> true
 *     dog -> chat   : perro is a dead end        -> false
 *     gato -> cat   : edges are directed         -> false
 *     cat -> cat    : a token translates to itself -> true
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * TRANSITIVE MAPPINGS = GRAPH REACHABILITY. Tokens are nodes, each mapping is
 * a directed edge, and "can I translate source into target" is simply "is
 * target reachable from source". Once you see that, it is a plain BFS.
 *
 * INVARIANT: any token that enters the queue is reachable from `source` via
 * the given mappings. So the moment the target is discovered, a valid chain
 * provably exists — no need to track the chain to answer yes/no.
 *
 * `visited` is what makes this safe, not just fast: a cycle a->b->c->a would
 * otherwise loop forever, and diamond-shaped mappings would re-explore the
 * same subgraph exponentially. Mark a token when ENQUEUING, not when polling,
 * or the same token can be queued several times before it is first processed.
 *
 * Note this is reachability, NOT a topological sort — cycles are legal input
 * here (two tokens that translate to each other), they just must not be
 * re-walked.
 *
 * APPROACHES
 *   Brute force : enumerate every translation chain from source. Cycles make
 *                 it non-terminating without visited, and even on a DAG the
 *                 number of distinct paths is exponential.
 *   Optimal     : adjacency list + one BFS (below).
 *
 * FOLLOW-UPS
 *   (1) Return the actual chain, e.g. cat -> gato -> chat -> neko.
 *       Implemented as `translationPath` — BFS parent pointers, walk back from
 *       the target, reverse. BFS also makes it the SHORTEST chain for free.
 *   (2) Weighted mappings (translation cost / confidence) -> best path.
 *       NOT implemented — same graph, but swap the queue for a priority queue
 *       and it becomes Dijkstra (or -log(confidence) edges if you want to
 *       MAXIMISE a product of confidences, turning it into a min-sum). Know
 *       the one-liner: "unweighted reachability is BFS, weighted best-path is
 *       Dijkstra on the same adjacency list."
 *
 * COMPLEXITY
 *   Let V = distinct tokens, E = mappings.
 *   Time  O(V + E)
 *   Space O(V + E)
 * ----------------------------------------------------------------------------
 */
public class Translator {

    public boolean canTranslate(
            List<List<String>> mappings,
            String source,
            String target) {

        if(source.equals(target))
        {
            return true;
        }

        Map<String, Set<String>> graph = new HashMap<>();
        for(List<String> mapping: mappings)
        {
            String u = mapping.get(0);
            String v = mapping.get(1);
            graph.computeIfAbsent(u, k -> new HashSet<>()).add(v);
            graph.computeIfAbsent(v, k -> new HashSet<>());
        }

        //we got a graph;
        Deque<String> q = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        visited.add(source);
        q.offer(source);
        while(!q.isEmpty())
        {
            String cur = q.poll();
            if(cur.equals(target))
            {
                return true;
            }

            for(String nei: graph.getOrDefault(cur, Collections.emptySet()))
            {
                if(!visited.contains(nei))
                {
                    q.offer(nei);
                    visited.add(nei);
                }
            }
        }
        return false;
    }

    /**
     * Follow-up (1): the shortest translation chain, or an empty list if the
     * target is unreachable.
     */
    public List<String> translationPath(
            List<List<String>> mappings,
            String source,
            String target) {

        if (source.equals(target)) {
            return List.of(source);
        }

        Map<String, Set<String>> graph = buildGraph(mappings);

        // parent doubles as the visited set; source maps to null.
        Map<String, String> parent = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        queue.offer(source);
        parent.put(source, null);

        while (!queue.isEmpty()) {

            String current = queue.poll();

            for (String neighbor :
                    graph.getOrDefault(current, Collections.emptySet())) {

                if (parent.containsKey(neighbor)) {
                    continue;
                }

                parent.put(neighbor, current);

                if (neighbor.equals(target)) {
                    return rebuildChain(parent, target);
                }

                queue.offer(neighbor);
            }
        }

        return List.of();
    }

    private Map<String, Set<String>> buildGraph(List<List<String>> mappings) {
        Map<String, Set<String>> graph = new HashMap<>();

        for (List<String> mapping : mappings) {
            graph.computeIfAbsent(mapping.get(0), key -> new HashSet<>())
                 .add(mapping.get(1));
        }

        return graph;
    }

    private List<String> rebuildChain(Map<String, String> parent, String target) {
        List<String> chain = new ArrayList<>();
        String current = target;

        while (current != null) {
            chain.add(current);
            current = parent.get(current);
        }

        Collections.reverse(chain);
        return chain;
    }

    public static void main(String[] args) {
        Translator solution = new Translator();

        List<List<String>> mappings = List.of(
                List.of("cat", "gato"),
                List.of("gato", "chat"),
                List.of("chat", "neko"),
                List.of("dog", "perro"));

        check("chained translation is reachable",
                solution.canTranslate(mappings, "cat", "neko"), true);

        check("direct mapping",
                solution.canTranslate(mappings, "cat", "gato"), true);

        // perro has no outgoing mapping, so the dog component is a dead end.
        check("separate component is unreachable",
                solution.canTranslate(mappings, "dog", "chat"), false);

        // Edges are directed: cat -> gato does not give gato -> cat.
        check("reverse direction is not implied",
                solution.canTranslate(mappings, "gato", "cat"), false);

        check("token translates to itself",
                solution.canTranslate(mappings, "cat", "cat"), true);

        check("unknown source",
                solution.canTranslate(mappings, "bird", "cat"), false);

        // a -> b -> c -> a; visited is what stops this from looping forever.
        List<List<String>> cyclic = List.of(
                List.of("a", "b"),
                List.of("b", "c"),
                List.of("c", "a"));

        check("cycle terminates and finds the target",
                solution.canTranslate(cyclic, "a", "c"), true);

        check("cycle terminates when the target is absent",
                solution.canTranslate(cyclic, "a", "d"), false);

        // --- follow-up (1): the actual chain ---

        checkPath("full chain is reconstructed",
                solution.translationPath(mappings, "cat", "neko"),
                List.of("cat", "gato", "chat", "neko"));

        checkPath("unreachable target yields no chain",
                solution.translationPath(mappings, "dog", "chat"),
                List.of());

        checkPath("self translation is a one-token chain",
                solution.translationPath(mappings, "cat", "cat"),
                List.of("cat"));

        // Two routes to d; BFS must return the shorter one.
        List<List<String>> branching = List.of(
                List.of("a", "b"),
                List.of("b", "d"),
                List.of("a", "c"),
                List.of("c", "x"),
                List.of("x", "d"));

        checkPath("BFS returns the shortest chain",
                solution.translationPath(branching, "a", "d"),
                List.of("a", "b", "d"));

        System.out.println("all passed");
    }

    private static void check(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }

    private static void checkPath(String name, List<String> actual, List<String> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
