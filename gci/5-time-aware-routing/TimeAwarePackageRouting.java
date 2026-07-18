/*
 * ============================================================================
 * Problem 5 (Google L4 prep) — Time-Aware Package / Flight Routing
 * ============================================================================
 *
 * THE QUESTION
 * ------------
 * Each flight is (source, destination, departureTime, arrivalTime). The package
 * starts at the source airport at time 0. A flight is catchable only when
 *      flight.departureTime >= currentArrivalTime.
 * Return whether the destination is reachable.
 *
 * Reported Google follow-up (NOT solved here): return the actual sequence of
 * flights/airports used. Not part of this row's stated scope; can be added by
 * storing a parent pointer per airport.
 *
 * EXAMPLE
 *   A->B dep1 arr10 ; A->B dep2 arr4 ; B->C dep5 arr7
 *   Reach C? yes: A->B(arr4) then B->C(dep5>=4). The arr10 A->B is too late.
 *
 * ----------------------------------------------------------------------------
 * MENTAL MAP  (the part to remember)
 * ----------------------------------------------------------------------------
 * Pattern: time-dependent graph + Dijkstra-style EARLIEST-ARRIVAL.
 * Store earliestArrival[airport]; arriving earlier always dominates (you can
 * wait). Process airports by earliest arrival with a min-heap.
 *
 * Why not a plain visited[] boolean: it can't tell whether B was reached at
 * time 4 or 10 — and only the earliest matters for catching later flights.
 * Guard stale heap entries: skip when state.arrivalTime != earliestArrival[air].
 *
 * APPROACHES
 *   Brute force : DFS every flight sequence -> exponential; re-explores airports
 *                 at worse times.
 *   Optimal     : min-heap by arrival time, relax outgoing flights (below).
 *                 Time O((A+F) log F)   Space O(A+F).
 * ----------------------------------------------------------------------------
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TimeAwarePackageRouting {

    static class Flight {
        String source;
        String destination;
        int departureTime;
        int arrivalTime;

        Flight(String source, String destination, int departureTime, int arrivalTime) {
            this.source = source;
            this.destination = destination;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
        }
    }

    static class State {
        String airport;
        int arrivalTime;

        State(String airport, int arrivalTime) {
            this.airport = airport;
            this.arrivalTime = arrivalTime;
        }
    }

    public boolean canReach(String source, String destination, List<Flight> flights) {
        if (source.equals(destination)) {
            return true;
        }

        // airport -> flights departing from it.
        Map<String, List<Flight>> graph = new HashMap<>();
        for (Flight flight : flights) {
            graph.computeIfAbsent(flight.source, key -> new ArrayList<>()).add(flight);
        }

        // Earliest time the package can be at each airport.
        Map<String, Integer> earliestArrival = new HashMap<>();
        earliestArrival.put(source, 0);

        PriorityQueue<State> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(state -> state.arrivalTime));
        minHeap.offer(new State(source, 0));

        while (!minHeap.isEmpty()) {
            State current = minHeap.poll();

            // Stale entry: a better arrival at this airport already settled.
            if (current.arrivalTime != earliestArrival.get(current.airport)) {
                continue;
            }

            // Processed by earliest arrival -> first pop of destination wins.
            if (current.airport.equals(destination)) {
                return true;
            }

            for (Flight flight : graph.getOrDefault(current.airport, List.of())) {
                // Can only board a flight that has not yet departed.
                if (flight.departureTime < current.arrivalTime) {
                    continue;
                }

                int previousBestArrival =
                        earliestArrival.getOrDefault(flight.destination, Integer.MAX_VALUE);

                // Keep only routes that reach the next airport earlier.
                if (flight.arrivalTime < previousBestArrival) {
                    earliestArrival.put(flight.destination, flight.arrivalTime);
                    minHeap.offer(new State(flight.destination, flight.arrivalTime));
                }
            }
        }

        return false;
    }

    // ------------------------------------------------------------------
    // Quick self-test.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        TimeAwarePackageRouting sol = new TimeAwarePackageRouting();

        List<Flight> flights = List.of(
            new Flight("A", "B", 1, 10),
            new Flight("A", "B", 2, 4),
            new Flight("B", "C", 5, 7)
        );
        System.out.println(sol.canReach("A", "C", flights)); // true (via A->B arr4 -> B->C)

        // Only the slow A->B (arr10) exists -> misses B->C (dep5).
        List<Flight> flights2 = List.of(
            new Flight("A", "B", 1, 10),
            new Flight("B", "C", 5, 7)
        );
        System.out.println(sol.canReach("A", "C", flights2)); // false

        System.out.println(sol.canReach("A", "A", flights));  // true (same airport)
    }
}
