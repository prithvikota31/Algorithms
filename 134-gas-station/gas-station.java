class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int totalBalance = 0;
        int tank = 0;
        int start = 0;

        for (int i = 0; i < gas.length; i++) {
            int diff = gas[i] - cost[i];

            // Tracks whether the whole circle has enough gas overall.
            totalBalance += diff;

            // Tracks whether current candidate start can reach this point.
            tank += diff;

            // If tank goes negative, current start is dead.
            // Also every station before i is dead, because they would have even less help.
            if (tank < 0) {
                start = i + 1;
                tank = 0;
            }
        }

        return totalBalance < 0 ? -1 : start;
    }
}