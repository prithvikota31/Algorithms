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
 * Reported Google follow-up: return the actual sequence of airports used.
 * This is implemented by storing a parent pointer per airport.
 *
 * >>> FUTURE FOLLOW-UP (NOT solving today) <<<
 *   Reach-before-deadline variant: given a deadline T, return whether the
 *   destination can be reached with arrivalTime <= T (one report phrases it as
 *   returning the SHORTEST valid path that arrives before the deadline).
 *   Idea: same earliest-arrival Dijkstra; prune any state with arrivalTime > T,
 *   and on reaching destination check arrival <= T (combine with parent-pointer
 *   path reconstruction for the "shortest valid path" phrasing).
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

    // TODO:
    //  1. trivial: if source.equals(destination) return true.
    //  2. build graph: Map<String, List<Flight>> of source -> its flights
    //     (computeIfAbsent + add).
    //  3. earliestArrival: Map<String,Integer>, put(source, 0).
    //  4. min-heap of State by arrivalTime; offer (source, 0).
    //  5. loop while heap non-empty:
    //       - poll current
    //       - skip stale: if current.arrivalTime > earliestArrival[current.airport] continue
    //       - if current.airport == destination return true
    //       - for each outgoing flight with departureTime >= current.arrivalTime:
    //           if flight.arrivalTime < best known for its destination,
    //           update earliestArrival and offer the new State.
    //  6. return false if the heap empties without reaching destination.
public class TimeAwarePackageRouting {

    public static class Flight{
        String source;
        String dest;
        int arrivalTime;
        int departureTime;

        public Flight(String source, String dest, int departureTime, int arrivalTime)
        {
            this.source = source;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
            this.dest = dest;
        }
    }

    public static class CurrentPos{
        String airport;
        int arrivalTime;
        public CurrentPos(String airport, int arrivalTime)
        {
            this.airport = airport;
            this.arrivalTime = arrivalTime;
        }
    }

    //use minHeap in order of arrival times so packet can reach sooner and have more options
    public boolean canReach(String source, String destination, List<Flight> flights) {
        if(source.equals(destination))
        {
            return true;
        }
        //create a graph
        //airport network
        //each flight is an edge
        //Map<String, List<Flights>>
        Map<String, List<Flight>> graph = new HashMap<>();
        
        for(Flight flight: flights)
        {
            String src = flight.source;
            String dst = flight.dest;
            graph.computeIfAbsent(src, k -> new ArrayList<>()).add(flight);
            graph.computeIfAbsent(dst, k -> new ArrayList<>());
        }
        //lets have a visited set
        Map<String, Integer> earliestTimes = new HashMap<>(); //airpot -> earliest times
        
        //sorted based on earliest arrival time so it have more options to reach destination
        PriorityQueue<CurrentPos> pq = 
                new PriorityQueue<>((a, b) -> Integer.compare(a.arrivalTime, b.arrivalTime));
        pq.offer(new CurrentPos(source, 0));
        earliestTimes.put(source, 0);

        while(!pq.isEmpty())
        {
            CurrentPos cur = pq.poll();
            String curAirport = cur.airport;
            int curArrivaltime = cur.arrivalTime;
            if(curAirport.equals(destination))
            {
                return true;
            }
            if(earliestTimes.getOrDefault(curAirport, Integer.MAX_VALUE) < curArrivaltime)
            {
                continue;
            }
            

            for(Flight nei: graph.getOrDefault(curAirport, Collections.emptyList()))
            {
                String neiDestination = nei.dest;
                int neiArrival = nei.arrivalTime;
                int neiDeparture = nei.departureTime;
                //only if curArrival time < neiDepaturetime it works
                if(curArrivaltime <= neiDeparture && neiArrival < earliestTimes.getOrDefault(neiDestination, Integer.MAX_VALUE))
                {
                    earliestTimes.put(neiDestination, neiArrival);
                    pq.offer(new CurrentPos(neiDestination, neiArrival));
                }
            }
        }
        return false;
           
    }

     //use minHeap in order of arrival times so packet can reach sooner and have more options
    public List<String> findBestPath(String source, String destination, List<Flight> flights) {
        if(source.equals(destination))
        {
            return Arrays.asList(source);
        }
        //create a graph
        //airport network
        //each flight is an edge
        //Map<String, List<Flights>>
        Map<String, List<Flight>> graph = new HashMap<>();
        
        for(Flight flight: flights)
        {
            String src = flight.source;
            String dst = flight.dest;
            graph.computeIfAbsent(src, k -> new ArrayList<>()).add(flight);
            graph.computeIfAbsent(dst, k -> new ArrayList<>());
        }
        //lets have a visited set
        Map<String, Integer> earliestTimes = new HashMap<>(); //airpot -> earliest times
        
        //sorted based on earliest arrival time so it have more options to reach destination
        PriorityQueue<CurrentPos> pq = 
                new PriorityQueue<>((a, b) -> Integer.compare(a.arrivalTime, b.arrivalTime));
        pq.offer(new CurrentPos(source, 0));
        earliestTimes.put(source, 0);
        Map<String, String> parentMap = new HashMap<>(); //child -> parent
        parentMap.put(source, source);

        while(!pq.isEmpty())
        {
            CurrentPos cur = pq.poll();
            String curAirport = cur.airport;
            int curArrivaltime = cur.arrivalTime;
            if(curAirport.equals(destination))
            {
                break;
            }
            if(earliestTimes.getOrDefault(curAirport, Integer.MAX_VALUE) < curArrivaltime)
            {
                continue;
            }
            

            for(Flight nei: graph.getOrDefault(curAirport, Collections.emptyList()))
            {
                String neiDestination = nei.dest;
                int neiArrival = nei.arrivalTime;
                int neiDeparture = nei.departureTime;
                //only if curArrival time < neiDepaturetime it works
                if(curArrivaltime <= neiDeparture && neiArrival < earliestTimes.getOrDefault(neiDestination, Integer.MAX_VALUE))
                {
                    earliestTimes.put(neiDestination, neiArrival);
                    pq.offer(new CurrentPos(neiDestination, neiArrival));
                    parentMap.put(neiDestination, curAirport);
                }
            }
        }

        List<String> path = new ArrayList<>();

        String cur = destination;
        if (!parentMap.containsKey(destination)) {
            return new ArrayList<>();
        }

        while(!parentMap.get(cur).equals(cur))
        {
            path.add(cur);
            cur = parentMap.get(cur);
        }
        path.add(source);
        Collections.reverse(path);
        
        return path;
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

        // ---- findBestPath tests ----
        System.out.println("--- findBestPath ---");

        // 1) basic 2-hop; must pick catchable A->B(arr4), not A->B(arr10)
        System.out.println(sol.findBestPath("A", "C", flights));   // [A, B, C]

        // 2) unreachable: slow A->B(arr10) misses B->C(dep5)
        System.out.println(sol.findBestPath("A", "C", flights2));  // []

        // 3) source == destination
        System.out.println(sol.findBestPath("A", "A", flights));   // [A]

        // 4) two routes to D; earliest arrival wins (via B arr6, not via C arr10)
        List<Flight> flights3 = List.of(
            new Flight("A", "B", 0, 5),
            new Flight("A", "C", 0, 3),
            new Flight("B", "D", 5, 6),
            new Flight("C", "D", 3, 10)
        );
        System.out.println(sol.findBestPath("A", "D", flights3));  // [A, B, D]

        // 5) direct single hop
        List<Flight> flights4 = List.of(new Flight("A", "B", 0, 4));
        System.out.println(sol.findBestPath("A", "B", flights4));  // [A, B]

        // 6) longer chain A->B->C->D, all catchable
        List<Flight> flights5 = List.of(
            new Flight("A", "B", 1, 2),
            new Flight("B", "C", 2, 3),
            new Flight("C", "D", 3, 4)
        );
        System.out.println(sol.findBestPath("A", "D", flights5));  // [A, B, C, D]
    }
}
