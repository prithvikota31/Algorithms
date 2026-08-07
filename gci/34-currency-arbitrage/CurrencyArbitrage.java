import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * ============================================================================
 * Problem 34 (Google L4 prep) — Currency Arbitrage Detection
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Given an n x n matrix of exchange rates (rates[from][to] > 0 means an
 * exchange exists, <= 0 means it doesn't), determine whether an ARBITRAGE
 * CYCLE exists: a sequence of trades starting and ending at the same
 * currency that leaves you with strictly MORE money than you started with.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   A cycle is profitable when the PRODUCT of its rates is > 1 (not the sum
 *   — exchange rates compound multiplicatively). Floating-point log() isn't
 *   exact, so an epsilon guard is needed to avoid flagging rounding noise as
 *   a real arbitrage opportunity.
 *
 * EXAMPLE
 *   USD->EUR = 0.9, EUR->GBP = 0.9, GBP->USD = 1.3
 *   1 USD -> 0.9 EUR -> 0.81 GBP -> 1.053 USD   (0.9*0.9*1.3 = 1.053 > 1)
 *   -> arbitrage exists, cycle = [USD, EUR, GBP, USD]
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: LOG-TRANSFORM MULTIPLICATION INTO ADDITION, THEN BELLMAN-FORD
 * NEGATIVE-CYCLE DETECTION.
 *
 *   Arbitrage exists  <=>  some cycle's rates multiply to > 1
 *                     <=>  log(r1) + log(r2) + ... + log(rk) > 0     (logs)
 *                     <=>  -log(r1) + -log(r2) + ... + -log(rk) < 0  (negate)
 *
 * So: weight every edge as -log(rate), and "arbitrage exists" becomes
 * "does this graph contain a negative-weight cycle?" — exactly what
 * Bellman-Ford detects. (Dijkstra can't be used: transformed weights can be
 * negative.)
 *
 * Initialize dist[i] = 0 for EVERY node (equivalent to a virtual source
 * connected to every currency by a free edge) so Bellman-Ford checks for a
 * negative cycle ANYWHERE in the graph, not just reachable from one node.
 *
 * Bellman-Ford invariant: after k full relaxation rounds, dist[v] reflects
 * the best cost using at most k edges. Any simple (cycle-free) path has at
 * most n-1 edges, so if an edge STILL relaxes on round n, only a negative
 * cycle can explain it.
 *
 * RECONSTRUCTING THE CYCLE ITSELF: take the node improved on that extra
 * round, then follow `parent` backward n MORE times first. By the pigeonhole
 * principle this guarantees landing strictly inside the cycle (a path
 * outside the cycle has fewer than n edges). From there, walk `parent`
 * again until returning to that same node — that walk IS the cycle.
 *
 * APPROACHES
 *   Brute force : enumerate every cycle, multiply its rates. O(cycles),
 *                 which can be exponential.
 *   Optimal     : Bellman-Ford after the -log transform (below).
 *
 * COMPLEXITY
 *   Time O(V * E) = O(V^3) for a dense/complete rate matrix   Space O(V)
 * ----------------------------------------------------------------------------
 */
public class CurrencyArbitrage {

    private static final double EPSILON = 1e-12;

    public boolean hasArbitrage(double[][] rates) {
        return !findArbitrageCycle(rates).isEmpty();
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): return the actual arbitrage cycle, not just
     * whether one exists.
     *
     * MENTAL MAP
     *   Same Bellman-Ford scan, but track `parent[to] = from` on every
     *   relaxation. Once a negative cycle is confirmed (something still
     *   relaxes on the extra round), walk `parent` backward n times to land
     *   inside the cycle, then trace the cycle from there.
     * ------------------------------------------------------------------------
     */
    public List<Integer> findArbitrageCycle(double[][] rates) {
        int n = rates.length;
        if (n == 0) {
            return new ArrayList<>();
        }

        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        // n - 1 rounds settle every negative-cycle-free (simple) path.
        for (int round = 0; round < n - 1; round++) {
            relaxAllEdges(rates, dist, parent);
        }

        // One more round: anything that still improves must be on, or
        // reachable from, a negative cycle.
        int cycleNode = relaxAllEdges(rates, dist, parent);
        if (cycleNode == -1) {
            return new ArrayList<>();
        }

        // Walk back n times to guarantee we've actually entered the cycle.
        int node = cycleNode;
        for (int i = 0; i < n; i++) {
            node = parent[node];
        }

        List<Integer> cycle = new ArrayList<>();
        int current = node;
        do {
            cycle.add(current);
            current = parent[current];
        } while (current != node);
        cycle.add(node);

        Collections.reverse(cycle);
        return cycle;
    }

    // Relaxes every edge once; returns the last "to" node improved, or -1.
    private int relaxAllEdges(double[][] rates, double[] dist, int[] parent) {
        int n = rates.length;
        int lastImproved = -1;

        for (int from = 0; from < n; from++) {
            for (int to = 0; to < n; to++) {
                double rate = rates[from][to];
                if (rate <= 0) {
                    continue;
                }

                double weight = -Math.log(rate);
                if (dist[from] + weight < dist[to] - EPSILON) {
                    dist[to] = dist[from] + weight;
                    parent[to] = from;
                    lastImproved = to;
                }
            }
        }

        return lastImproved;
    }

    public static void main(String[] args) {
        CurrencyArbitrage solution = new CurrencyArbitrage();

        // USD=0, EUR=1, GBP=2. USD->EUR->GBP->USD compounds to 1.053 > 1.
        double[][] arbitrageRates = {
                { 0, 0.9, 0 },
                { 0, 0, 0.9 },
                { 1.3, 0, 0 }
        };
        check("arbitrage cycle detected", solution.hasArbitrage(arbitrageRates), true);
        check("arbitrage cycle reconstructed",
                solution.findArbitrageCycle(arbitrageRates), List.of(0, 1, 2, 0));

        // Same currencies, but the round trip loses money (0.9 * 1.05 = 0.945 < 1).
        double[][] noArbitrageRates = {
                { 0, 0.9 },
                { 1.05, 0 }
        };
        check("no arbitrage detected", solution.hasArbitrage(noArbitrageRates), false);
        check("no arbitrage cycle to reconstruct",
                solution.findArbitrageCycle(noArbitrageRates), List.of());

        check("empty rate matrix", solution.hasArbitrage(new double[0][0]), false);
        check("single currency, no edges", solution.hasArbitrage(new double[][] { { 0 } }), false);

        System.out.println("all passed");
    }

    private static void check(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }

    private static void check(String name, List<Integer> actual, List<Integer> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
