/*
 * ============================================================================
 * Problem 16 follow-up (Google L4 prep) — Product of the Last K Numbers
 *                                          [LeetCode 1352]
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Now K is NOT fixed in the constructor. Support:
 *      add(int value)
 *      getProduct(int k)   // product of the last k values, O(1)
 * Assume k never exceeds the number of inserted values.
 *
 * EXAMPLE
 *      add(3); add(0); add(2); add(5); add(4);
 *      getProduct(2) -> 5 * 4       = 20
 *      getProduct(3) -> 2 * 5 * 4   = 40
 *      getProduct(4) -> 0 * 2 * 5 * 4 = 0
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: PREFIX PRODUCT (like prefix sum).
 *      prefix[i] = product of the first i values
 *      product of range [L, R] = prefix[R+1] / prefix[L]
 *      -> last k values = prefix[last] / prefix[last - k]
 *
 * The zero problem: a 0 makes every later prefix product 0, so division dies.
 *
 * The trick: a zero CUTS the stream into independent zero-free segments.
 * Keep prefix products only for values AFTER the most recent zero. On add(0),
 * RESET the list to just [1].
 *
 * Invariant:
 *      prefixProducts holds cumulative products since the most recent zero,
 *      and always starts with the identity 1.
 *      => if k >= prefixProducts.size(), the window crosses the last zero,
 *         so the answer is 0.
 *      => else answer = prefix[last] / prefix[last - k].
 *
 * Insight to bank: resetting at zero converts a hard GLOBAL prefix-product
 * problem into an ordinary prefix product over the current zero-free segment.
 *
 * APPROACHES
 *   Brute force : store all values; multiply last k per query.
 *                 add O(1), getProduct O(k), space O(N).
 *   Optimal     : prefix products with zero-reset.
 *                 add O(1), getProduct O(1), space O(N)
 *                 (O(N) because future k may reach far back).
 *
 * DRY RUN
 *   add(3) prefix=[1,3]
 *   add(0) prefix=[1]            (reset)
 *   add(2) prefix=[1,2]
 *   add(5) prefix=[1,2,10]
 *   add(4) prefix=[1,2,10,40]
 *   getProduct(2): 40 / prefix[4-2]=prefix[2]=2 -> 20   (= 5*4)
 *   getProduct(4): k=4 >= size 4 -> window crosses the zero -> 0
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayList;
import java.util.List;

public class ProductOfNumbers {

    // prefixProducts[i] = product of the first i values added after the most
    // recent zero. Index 0 holds the identity 1 (empty prefix).
    private final List<Long> prefixProducts;

    public ProductOfNumbers() {
        prefixProducts = new ArrayList<>();
        prefixProducts.add(1L);
    }

    public void add(int value) {
        // A zero ends the current non-zero segment. Anything crossing it must
        // return 0, so older prefix products are no longer needed: reset.
        if (value == 0) {
            prefixProducts.clear();
            prefixProducts.add(1L);
            return;
        }
        long previousProduct = prefixProducts.get(prefixProducts.size() - 1);
        prefixProducts.add(previousProduct * value);
    }

    public long getProduct(int k) {
        // prefixProducts.size() - 1 non-zero values exist after the latest
        // zero. If fewer than k, the window must contain that zero.
        if (k >= prefixProducts.size()) {
            return 0;
        }
        int lastIndex = prefixProducts.size() - 1;
        return prefixProducts.get(lastIndex) / prefixProducts.get(lastIndex - k);
    }

    /*
     * ------------------------------------------------------------------------
     * ALTERNATIVE (my version): keep the full list, never clear it, and just
     * track the index of the most recent zero marker instead.
     *
     *   Model: 2, 8, 0, 3, 10          (values)
     *          index 0 1 2 3 4
     *   prefix: 1, 2, 16, 1, 3, 30     (size 6, indices 0..5)
     *          lastZero = 3, n = size - 1 = 5
     *   A query is valid iff its window start n-k lands at/after lastZero.
     *
     *   private final List<Long> prefixProducts;   // seeded with 1L
     *   int lastZero = 0;
     *
     *   void add(int value) {
     *       if (value == 0) {
     *           prefixProducts.add(1L);            // append a fresh identity
     *           lastZero = prefixProducts.size() - 1;
     *       } else {
     *           long last = prefixProducts.get(prefixProducts.size() - 1);
     *           prefixProducts.add(last * value);
     *       }
     *   }
     *
     *   long getProduct(int k) {
     *       int n = prefixProducts.size() - 1;
     *       if (n - k >= lastZero) {
     *           return prefixProducts.get(n) / prefixProducts.get(n - k);
     *       }
     *       return 0;
     *   }
     *
     * Both are O(1) per op / O(N) space and produce identical results; the
     * canonical version above just discards history at each zero.
     * ------------------------------------------------------------------------
     */

    // ------------------------------------------------------------------
    // Quick self-test (expected: 20, 40, 0).
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        ProductOfNumbers p = new ProductOfNumbers();
        p.add(3); p.add(0); p.add(2); p.add(5); p.add(4);
        System.out.println(p.getProduct(2)); // 20
        System.out.println(p.getProduct(3)); // 40
        System.out.println(p.getProduct(4)); // 0
    }
}
