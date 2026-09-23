/*
 * ============================================================================
 * Problem 15 Follow-up (Google L4 prep) — Assign Meetings to Minimum Rooms
 * ============================================================================
 *
 * PROMPT
 * ------
 * Given meetings as half-open intervals [start, end), assign every meeting to
 * a room while using the minimum possible number of rooms. Return one list per
 * room containing the original indices of the meetings assigned to that room.
 *
 * EXAMPLES
 *   [[0, 30], [5, 10], [15, 20]] -> [[0], [1, 2]]
 *   [[1, 3], [3, 5]]             -> [[0, 1]]
 *   []                            -> []
 *
 * INTUITION
 * ---------
 * Process meetings from earliest start to latest. Before assigning a meeting,
 * release every room whose current meeting has ended. If a room is free, reuse
 * it; otherwise create a new room. A min-heap ordered by end time tells us
 * exactly which occupied rooms can be released.
 *
 * This is optimal because a new room is created only when every existing room
 * overlaps the current meeting. At that instant, no valid assignment could use
 * fewer rooms.
 *
 * ALGORITHM
 * ---------
 * 1. Attach each meeting's original index and sort meetings by start time.
 * 2. Keep occupied rooms in a min-heap ordered by their meeting's end time.
 * 3. Release all rooms with end time <= the next meeting's start time.
 * 4. Reuse the smallest available room ID, or create a new room if none is free.
 * 5. Record the meeting's original index and mark the room occupied until end.
 *
 * COMPLEXITY
 * ----------
 * Time O(N log N)   Space O(N), where N is the number of meetings.
 * ============================================================================
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class MeetingRoomAssignment {

    public List<List<Integer>> minMeetingRooms(int[][] intervalsWithoutIndex) {
        if (intervalsWithoutIndex == null) {
            throw new IllegalArgumentException("Intervals must not be null");
        }

        int meetingCount = intervalsWithoutIndex.length;
        List<List<Integer>> rooms = new ArrayList<>();
        int[][] intervals = new int[meetingCount][3];

        for (int meetingId = 0; meetingId < meetingCount; meetingId++) {
            int[] interval = intervalsWithoutIndex[meetingId];
            if (interval == null || interval.length != 2 || interval[0] > interval[1]) {
                throw new IllegalArgumentException("Invalid interval at index " + meetingId);
            }

            intervals[meetingId][0] = interval[0];
            intervals[meetingId][1] = interval[1];
            intervals[meetingId][2] = meetingId;
        }

        Arrays.sort(intervals, (first, second) -> {
            int byStart = Integer.compare(first[0], second[0]);
            return byStart != 0 ? byStart : Integer.compare(first[2], second[2]);
        });

        // Entries are {endTime, roomId}; earliest finishing room is on top.
        PriorityQueue<int[]> occupiedRooms = new PriorityQueue<>(
                (first, second) -> Integer.compare(first[0], second[0]));
        PriorityQueue<Integer> availableRooms = new PriorityQueue<>();

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            int meetingId = interval[2];

            while (!occupiedRooms.isEmpty() && occupiedRooms.peek()[0] <= start) {
                availableRooms.offer(occupiedRooms.poll()[1]);
            }

            int roomId;
            if (availableRooms.isEmpty()) {
                roomId = rooms.size();
                rooms.add(new ArrayList<>());
            } else {
                roomId = availableRooms.poll();
            }

            rooms.get(roomId).add(meetingId);
            occupiedRooms.offer(new int[]{end, roomId});
        }

        return rooms;
    }

    public static void main(String[] args) {
        MeetingRoomAssignment solver = new MeetingRoomAssignment();

        assert solver.minMeetingRooms(new int[][]{
                {0, 30}, {5, 10}, {15, 20}
        }).equals(Arrays.asList(
                Arrays.asList(0),
                Arrays.asList(1, 2)));

        assert solver.minMeetingRooms(new int[][]{
                {1, 3}, {3, 5}
        }).equals(Arrays.asList(Arrays.asList(0, 1)));

        assert solver.minMeetingRooms(new int[][]{}).isEmpty();

        boolean threw = false;
        try {
            solver.minMeetingRooms(new int[][]{{4, 2}});
        } catch (IllegalArgumentException exception) {
            threw = true;
        }
        assert threw;

        System.out.println("All MeetingRoomAssignment tests passed.");
    }
}