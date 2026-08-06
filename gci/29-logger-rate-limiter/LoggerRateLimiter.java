import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * ============================================================================
 * Problem 29 (Google L4 prep) — Logger Rate Limiter
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Design a logger that suppresses a message if the SAME message was already
 * printed within the last `window` seconds. `shouldPrintMessage(timestamp,
 * message)` returns true (print) or false (suppress).
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   "Duplicate" means an exact string match. Only the most recent print time
 *   per message matters — older history for that message is irrelevant once
 *   a newer print time is recorded. `timestamp - lastPrinted >= window`
 *   (inclusive) counts as "enough time has passed."
 *
 * EXAMPLE (window = 10)
 *   (1, "error")   -> true   (first time seen)
 *   (5, "error")   -> false  (5 - 1 = 4  < 10)
 *   (12, "error")  -> true   (12 - 1 = 11 >= 10)
 *   (15, "timeout")-> true   (different message)
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: HASH MAP AS "LAST SEEN" CACHE.
 *
 *   lastPrintedTime[message] = the last timestamp this message was allowed
 *   to print.
 *
 * For each incoming (timestamp, message):
 *   - Not in the map yet             -> print, record timestamp.
 *   - timestamp - lastPrinted >= D   -> print, update to the new timestamp.
 *   - Otherwise                      -> suppress, map unchanged.
 *
 * Only the LATEST timestamp per message needs to be kept — an older print
 * time can never make a future decision "more permissive" than the latest
 * one already does.
 *
 * APPROACHES
 *   Brute force : keep every (message, timestamp) ever seen, scan backward
 *                 for a recent duplicate. O(n) per call.
 *   Optimal     : one hash-map lookup + update per call (below). O(1) time.
 *
 * COMPLEXITY
 *   Time O(1) per call   Space O(M), M = number of distinct messages
 * ----------------------------------------------------------------------------
 */
public class LoggerRateLimiter {

    private final Map<String, Integer> lastPrintedTime = new HashMap<>();
    private final int window;

    public LoggerRateLimiter(int window) {
        this.window = window;
    }

    public boolean shouldPrintMessage(int timestamp, String message) {
        Integer previousTime = lastPrintedTime.get(message);
        if (previousTime == null || timestamp - previousTime >= window) {
            lastPrintedTime.put(message, timestamp);
            return true;
        }
        return false;
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): make it thread-safe for multiple producers.
     *
     * MENTAL MAP
     *   A plain HashMap's get-then-put is two separate steps — another thread
     *   can slip in between them and both threads print the same message.
     *   ConcurrentHashMap.compute() makes "check previous time AND decide the
     *   new value" a SINGLE atomic operation per key, closing that race.
     * ------------------------------------------------------------------------
     */
    public static class ThreadSafe {

        private final ConcurrentHashMap<String, Integer> lastPrintedTime = new ConcurrentHashMap<>();
        private final int window;

        public ThreadSafe(int window) {
            this.window = window;
        }

        public boolean shouldPrintMessage(int timestamp, String message) {
            boolean[] allowed = new boolean[1];
            lastPrintedTime.compute(message, (key, previousTime) -> {
                if (previousTime == null || timestamp - previousTime >= window) {
                    allowed[0] = true;
                    return timestamp;
                }
                allowed[0] = false;
                return previousTime;
            });
            return allowed[0];
        }
    }

    public static void main(String[] args) {
        LoggerRateLimiter limiter = new LoggerRateLimiter(10);
        check("first occurrence", limiter.shouldPrintMessage(1, "error"), true);
        check("suppressed within window", limiter.shouldPrintMessage(5, "error"), false);
        check("exact boundary allowed", limiter.shouldPrintMessage(11, "error"), true);
        check("different message always allowed", limiter.shouldPrintMessage(15, "timeout"), true);
        check("suppressed again right after boundary print", limiter.shouldPrintMessage(12, "error"), false);

        LoggerRateLimiter.ThreadSafe threadSafeLimiter = new LoggerRateLimiter.ThreadSafe(10);
        check("thread-safe: first occurrence", threadSafeLimiter.shouldPrintMessage(1, "error"), true);
        check("thread-safe: suppressed within window", threadSafeLimiter.shouldPrintMessage(5, "error"), false);
        check("thread-safe: exact boundary allowed", threadSafeLimiter.shouldPrintMessage(11, "error"), true);

        System.out.println("all passed");
    }

    private static void check(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
        System.out.println("pass " + name + " -> " + actual);
    }
}
