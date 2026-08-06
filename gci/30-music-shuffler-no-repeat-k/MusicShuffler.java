import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/*
 * ============================================================================
 * Problem 30 (Google L4 prep) — Music Shuffler, No Repeat Within K Plays
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Design a shuffler that returns a song uniformly at random from a playlist,
 * except a song cannot be returned again if it was played within the last
 * K plays.
 *
 * AMBIGUITY TO CONFIRM WITH THE INTERVIEWER
 *   "Random" means uniform selection among the CURRENTLY VALID (unblocked)
 *   songs at that moment — not "keep retrying nextInt() until it's valid"
 *   (which can spin arbitrarily long as K approaches the playlist size).
 *
 * EXAMPLE
 *   songs=[A,B,C,D,E], K=2
 *   Valid:   A, C, B, D, A, E, ...
 *   Invalid: A, B, A   (A repeats after only 1 other song, needs >= 2)
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: COOLDOWN WINDOW = QUEUE (order) + SET (fast membership check).
 *
 *   recentSongs : Queue holding exactly the last K played songs, oldest first.
 *   blockedSongs: Set mirroring recentSongs, for O(1) "is this blocked?".
 *
 * Per play:
 *   1. available = playlist songs NOT in blockedSongs.
 *   2. Pick uniformly among `available` (not among the whole playlist).
 *   3. Add the pick to both recentSongs and blockedSongs.
 *   4. If recentSongs now holds more than K songs, evict the oldest from
 *      BOTH recentSongs and blockedSongs — it has aged out of the cooldown.
 *
 * Invariant: after every play, blockedSongs == exactly the songs played in
 * the last K plays (or fewer, near the start).
 *
 * TESTABILITY NOTE: inject the `Random` instance through the constructor so
 * tests can assert invariants (no-repeat-within-K, always a real playlist
 * song) deterministically instead of guessing exact "random" output.
 *
 * APPROACHES
 *   Brute force : shuffle-then-reject until a valid song appears. Can spin
 *                 for a long time as K approaches playlist size.
 *   Optimal     : maintain the cooldown window directly (below). O(n) time
 *                 per pick to scan the playlist for available songs (O(1)
 *                 with the HashMap<->ArrayList index swap noted below, but
 *                 that's an "understand only" optimization for this level).
 *
 * COMPLEXITY
 *   Time O(n) per pick, n = playlist size   Space O(n)
 * ----------------------------------------------------------------------------
 */
public class MusicShuffler {

    private final List<String> songs;
    private final int k;
    private final Queue<String> recentSongs = new LinkedList<>();
    private final Set<String> blockedSongs = new HashSet<>();
    private final Random random;

    public MusicShuffler(List<String> songs, int k) {
        this(songs, k, new Random());
    }

    public MusicShuffler(List<String> songs, int k, Random random) {
        if (songs == null || k < 0 || k >= songs.size()) {
            throw new IllegalArgumentException("k must be in [0, songs.size())");
        }
        this.songs = songs;
        this.k = k;
        this.random = random;
    }

    public String nextSong() {
        List<String> available = new ArrayList<>();
        for (String song : songs) {
            if (!blockedSongs.contains(song)) {
                available.add(song);
            }
        }

        String chosen = available.get(random.nextInt(available.size()));

        recentSongs.offer(chosen);
        blockedSongs.add(chosen);
        if (recentSongs.size() > k) {
            String expired = recentSongs.poll();
            blockedSongs.remove(expired);
        }

        return chosen;
    }

    /*
     * ------------------------------------------------------------------------
     * FOLLOW-UP (must prepare): weighted random selection, still respecting
     * the no-repeat-within-K constraint.
     *
     * MENTAL MAP
     *   Same cooldown bookkeeping as the base class. The only change is HOW
     *   we pick from `available`: sum the weights of the available songs,
     *   draw a random target in [0, totalWeight), then walk `available`
     *   accumulating weight until the running sum passes the target. The
     *   weights are re-summed over just the CURRENTLY available subset each
     *   time, since a blocked song's weight shouldn't count right now.
     * ------------------------------------------------------------------------
     */
    public static class Weighted {

        private final List<String> songs;
        private final Map<String, Double> weights;
        private final int k;
        private final Queue<String> recentSongs = new LinkedList<>();
        private final Set<String> blockedSongs = new HashSet<>();
        private final Random random;

        public Weighted(List<String> songs, Map<String, Double> weights, int k, Random random) {
            if (songs == null || k < 0 || k >= songs.size()) {
                throw new IllegalArgumentException("k must be in [0, songs.size())");
            }
            this.songs = songs;
            this.weights = weights;
            this.k = k;
            this.random = random;
        }

        public String nextSong() {
            List<String> available = new ArrayList<>();
            double totalWeight = 0;
            for (String song : songs) {
                if (!blockedSongs.contains(song)) {
                    available.add(song);
                    totalWeight += weights.get(song);
                }
            }

            double target = random.nextDouble() * totalWeight;
            double cumulative = 0;
            String chosen = available.get(available.size() - 1);
            for (String song : available) {
                cumulative += weights.get(song);
                if (target < cumulative) {
                    chosen = song;
                    break;
                }
            }

            recentSongs.offer(chosen);
            blockedSongs.add(chosen);
            if (recentSongs.size() > k) {
                String expired = recentSongs.poll();
                blockedSongs.remove(expired);
            }

            return chosen;
        }
    }

    public static void main(String[] args) {
        List<String> playlist = List.of("A", "B", "C", "D", "E");
        int k = 2;
        MusicShuffler shuffler = new MusicShuffler(playlist, k, new Random(42));

        List<String> history = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String next = shuffler.nextSong();
            check("pick " + i + " is a real playlist song", playlist.contains(next), true);
            int windowStart = Math.max(0, history.size() - k);
            for (int j = windowStart; j < history.size(); j++) {
                check("pick " + i + " doesn't repeat within last " + k + " plays",
                        next.equals(history.get(j)), false);
            }
            history.add(next);
        }
        System.out.println("all passed base invariants over " + history.size() + " picks");

        Map<String, Double> heavilyWeighted = Map.of("A", 1000.0, "B", 1.0);
        MusicShuffler.Weighted weightedShuffler =
                new MusicShuffler.Weighted(List.of("A", "B"), heavilyWeighted, 0, new Random(7));
        int countA = 0;
        int countB = 0;
        for (int i = 0; i < 1000; i++) {
            if (weightedShuffler.nextSong().equals("A")) {
                countA++;
            } else {
                countB++;
            }
        }
        check("heavily weighted song dominates selection", countA > countB, true);

        System.out.println("all passed");
    }

    private static void check(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new AssertionError("FAIL " + name + ": got " + actual + " want " + expected);
        }
    }
}
