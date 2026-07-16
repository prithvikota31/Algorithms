/*
 * ============================================================================
 * Problem 15 (Google L4 prep) — Top K Elements from a Stream
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * A stream continuously sends integers. After each insertion, retain the
 * K LARGEST numbers seen so far. K is fixed; duplicates count separately; the
 * current Top K can be requested at any time and is returned in descending order.
 * We can't store + re-sort the whole stream — it may be huge or infinite.
 *
 * EXAMPLE
 *   Stream: 5, 2, 10, 4, 8   K = 3   ->   [10, 8, 5]
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Ask: "which values do I actually need to keep?"  -> only the best K.
 * Use a MIN-HEAP of size K. Why min-heap? Among the current Top K, the SMALLEST
 * is the weakest member — put it at the root so it's cheap to replace.
 *   Invariant: the heap holds the K largest values seen so far, and its ROOT is
 *   the K-th largest.  Newcomer beats the root -> it enters the club.
 *
 * APPROACHES
 *   Brute force : store everything, sort on query. insert O(1), query O(N log N),
 *                 space O(N). Fails for a huge/infinite stream.
 *   Optimal     : min-heap of size K.
 *                   - heap has < K -> just insert.
 *                   - else if value > root -> replace root; otherwise ignore.
 *
 * COMPLEXITY
 *   add O(log K)   getTopK O(K log K)   getKthLargest O(1)   Space O(K)
 *
 * FOLLOW-UPS — verified core set is FOUR (Google-reported)
 *   [1 DONE ] K-th largest from an infinite stream -> getKthLargest() (heap root).
 *   [2 CODED] Top K by frequency (e.g. users by message count) -> TopKChatUsers.
 *             Two phases: count each key in a HashMap, THEN run Top-K over the
 *             (key,count) records. Can't discard a key early — its count may grow.
 *   [3 CODED] CONTINUOUS Top-K as frequencies change on a live stream ->
 *             TopKUsersStream. HashMap count + TreeSet ranked by (count desc,
 *             user asc). On each message: remove stale rank -> bump count ->
 *             re-insert. Never mutate a count while its object is in the TreeSet.
 *             add O(log U), topK O(K).
 *   [4 EXPLAIN] Dataset larger than memory (scaling). Hash-partition the input,
 *             count ONE partition at a time, and maintain a global min-heap of
 *             size K across partitions. Usually explanation only — no disk-I/O
 *             code needed.
 *
 *   Clarification (NOT a distinct follow-up): ties at the K-th position — either
 *   break ties alphabetically, or include everyone tied with the K-th frequency.
 * ----------------------------------------------------------------------------
 */

import java.util.*;

public class TopKFromStream {

    private final int k;

    // Contains only the K largest values seen so far.
    // The root is the smallest among them: the current K-th largest.
    private final PriorityQueue<Integer> minHeap;

    public TopKFromStream(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("K must be positive");
        }
        this.k = k;
        this.minHeap = new PriorityQueue<>();
    }

    public void add(int value) {
        // Add unconditionally, then if we're over capacity drop the root
        // (the smallest) — it just fell out of the Top K.
        minHeap.offer(value);
        if (minHeap.size() > k) {
            minHeap.poll();
        }
    }

    public List<Integer> getTopK() {
        // Heap order is not fully sorted, so copy and sort for presentation.
        List<Integer> result = new ArrayList<>(minHeap);
        result.sort(Collections.reverseOrder()); //klogk
        return result;
    }

    public Integer getKthLargest() {
        if (minHeap.size() < k) {
            return null;
        }
        return minHeap.peek();
    }

    // ==================================================================
    // FOLLOW-UP #2: Top K users by message count. O(N + U log K).
    // Phase 1: count each user. Phase 2: Top-K over the unique users.
    // (N = messages, U = unique users.)
    // ==================================================================
    static class TopKChatUsers {

        public List<String> topKUsers(List<String[]> messages, int k) {
            if (k <= 0) {
                return Collections.emptyList();
            }

            Map<String, Integer> frequency = new HashMap<>();

            // First count how many messages each user sent.
            for (String[] message : messages) {
                String username = message[0];
                frequency.put(username, frequency.getOrDefault(username, 0) + 1);
            }

            // Root is the weakest user (smallest count) currently in the Top K.
            // Heap holds usernames; the comparator looks up each user's count.
            PriorityQueue<String> minHeap =
                new PriorityQueue<>((a, b) -> Integer.compare(frequency.get(a), frequency.get(b)));

            // Apply Top K over the unique users, not over every message.
            for (String user : frequency.keySet()) {
                minHeap.offer(user);
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            // Heap pops weakest-first, so collect then reverse -> strongest first.
            List<String> result = new ArrayList<>();
            while (!minHeap.isEmpty()) {
                result.add(minHeap.poll());
            }
            Collections.reverse(result);
            return result;
        }
    }

    // ==================================================================
    // FOLLOW-UP #3: CONTINUOUS Top-K over a live message stream.
    // HashMap count + TreeSet ranked by (count desc, user asc).
    // addMessage O(log U), getTopK O(K), space O(U).
    // ==================================================================
    static class TopKUsersStream {

        // user -> latest message count
        private final Map<String, Integer> frequency = new HashMap<>();

        // Users ordered by:
        // 1. Higher count first
        // 2. Alphabetical username for equal counts
        private final TreeSet<UserCount> ranking =
            new TreeSet<>((a, b) -> {
                if (a.count != b.count) {
                    return Integer.compare(b.count, a.count);
                }
                return a.user.compareTo(b.user);
            });

        public void addMessage(String user) {
            int oldCount = frequency.getOrDefault(user, 0);

            // Remove the stale object before changing the user's count.
            if (oldCount > 0) {
                ranking.remove(new UserCount(user, oldCount));
            }

            int newCount = oldCount + 1;
            frequency.put(user, newCount);

            // Insert a new object at the correct ranked position.
            ranking.add(new UserCount(user, newCount));
        }

        public List<String> getTopK(int k) {
            List<String> result = new ArrayList<>();

            // TreeSet iteration already follows ranking order.
            for (UserCount entry : ranking) {
                if (result.size() == k) {
                    break;
                }
                result.add(entry.user);
            }

            return result;
        }

        private static class UserCount {
            String user;
            int count;

            UserCount(String user, int count) {
                this.user = user;
                this.count = count;
            }
        }
    }

    // ------------------------------------------------------------------
    // Quick self-test (the dry run).
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        TopKFromStream topK = new TopKFromStream(3);
        for (int v : new int[]{5, 2, 10, 4, 8}) topK.add(v);
        System.out.println(topK.getTopK());        // [10, 8, 5]
        System.out.println(topK.getKthLargest());  // 5

        // FOLLOW-UP #2: Top K users by message count.
        List<String[]> messages = new ArrayList<>();
        messages.add(new String[]{"Alice", "Hi"});
        messages.add(new String[]{"Bob", "Hello"});
        messages.add(new String[]{"Alice", "How are you?"});
        messages.add(new String[]{"Sam", "Hey"});
        messages.add(new String[]{"Alice", "Bye"});
        messages.add(new String[]{"Bob", "Bye"});
        System.out.println(new TopKChatUsers().topKUsers(messages, 2)); // [Alice, Bob]

        // FOLLOW-UP #3: continuous Top-K over a live message stream.
        TopKUsersStream stream = new TopKUsersStream();
        for (String u : new String[]{"Alice", "Bob", "Alice", "Sam", "Bob", "Alice"}) {
            stream.addMessage(u);
        }
        System.out.println(stream.getTopK(2)); // [Alice, Bob]
    }
}
