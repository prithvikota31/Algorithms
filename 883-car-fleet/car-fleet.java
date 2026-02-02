import java.util.*;

class Solution {
    public int carFleet(int target, int[] position, int[] speed) {
        int n = position.length;
        int[][] cars = new int[n][2];

        for (int i = 0; i < n; i++) {
            cars[i][0] = position[i];
            cars[i][1] = speed[i];
        }

        // Sort by position descending (closest to target first)
        Arrays.sort(cars, (a, b) -> b[0] - a[0]);

        Deque<Double> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            double time = (double)(target - cars[i][0]) / cars[i][1];

            // If this car catches the fleet ahead, merge (discard it)
            if (!stack.isEmpty() && time <= stack.peek()) {
                continue;
            }

            // Otherwise, start a new fleet
            stack.push(time);
        }

        return stack.size();
    }
}
