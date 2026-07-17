/*
 * ============================================================================
 * Problem 16 (Google L4 prep) — Product of the Last K Values in a Stream
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Design a stream structure with:
 *      add(x)         // add x to the stream
 *      getProduct()   // product of the latest K values; null until K exist
 *
 * Fixed K. (Follow-ups, NOT solved here: dynamic getProduct(k); time-window
 * unique values; top-K statistics. We do only the fixed-K product base.)
 *
 * EXAMPLE  (K = 3)
 *      add(4); add(5); add(10); add(-1);
 *      getProduct() -> 5 * 10 * -1 = -50
 *      add(3);
 *      getProduct() -> 10 * -1 * 3 = -30
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: FIXED-SIZE SLIDING WINDOW + rolling aggregate.
 *
 * Instead of remultiplying K values per query, maintain the product as values
 * enter and leave:
 *      newProduct = oldProduct * incoming / outgoing
 *
 * BUT zero breaks division. So track zeros separately:
 *      nonZeroProduct = product of all non-zero values in the window
 *      zeroCount      = how many zeros are in the window
 *      zeroCount > 0  -> product is 0
 *      zeroCount == 0 -> product is nonZeroProduct
 *
 * Division is exact for a non-zero outgoing value because
 *      nonZeroProduct = outgoing * (all other values)
 * (Assumes every product fits in long.)
 *
 * APPROACHES
 *   Brute force : keep last K in a queue; multiply all K per query.
 *                 add O(1), getProduct O(K), space O(K).
 *   Optimal     : rolling nonZeroProduct + zeroCount.
 *                 add O(1), getProduct O(1), space O(K).
 *
 * DRY RUN  (K = 3)
 *   add(4)  win=[4]           product=4     get=null
 *   add(5)  win=[4,5]         product=20    get=null
 *   add(10) win=[4,5,10]      product=200   get=200
 *   add(-1) win=[5,10,-1]     product=-50   get=-50   (dropped 4: -200/4)
 *   add(3)  win=[10,-1,3]     product=-30   get=-30   (dropped 5: -150/5)
 *
 * Note: for MEAN, keep a rolling sum instead. Arbitrary statistics (e.g.
 * median) need statistic-specific structures, not just a swapped aggregate.
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayDeque;
import java.util.Deque;

public class LastKProduct {


    private Deque<Integer> window;
    private int zeroCount;
    private long nonZeroProduct;
    private int k;
    public LastKProduct(int k) {

        if(k <= 0)
        {
            throw new IllegalArgumentException("K must be positive");
        }
        window = new ArrayDeque<>();
        this.k = k;
        zeroCount = 0;
        nonZeroProduct = 1;
    }

    public void add(int value) {
        window.offer(value);
        if(value == 0)
        {
            zeroCount++;
        }
        else{
            nonZeroProduct *= value;
        }
        
        if(window.size() > k)
        {
            int removalValue = window.pollFirst();
            if(removalValue == 0)
            {
                zeroCount--;
            }
            else{
                nonZeroProduct = nonZeroProduct / removalValue;
            }
        }
    }

    public Long getProduct() {
        if(window.size() < k)
        {
            return null;
        }

        if(zeroCount > 0)
        {
            return 0L;
        }
        else{
            return nonZeroProduct;
        }
    }

    // ------------------------------------------------------------------
    // Quick self-test (expected: null, null, 200, -50, -30).
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        LastKProduct s = new LastKProduct(3);
        s.add(4);  System.out.println(s.getProduct()); // null
        s.add(5);  System.out.println(s.getProduct()); // null
        s.add(10); System.out.println(s.getProduct()); // 200
        s.add(-1); System.out.println(s.getProduct()); // -50
        s.add(3);  System.out.println(s.getProduct()); // -30
    }
}
