class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int totalDiff = 0;
        int currentTank = 0;
        int start = 0;

        for(int i = 0; i < gas.length; i++)
        {
            totalDiff += gas[i] - cost[i];
            currentTank += gas[i] - cost[i];

            if(currentTank < 0) //reset the start position and ignore all the previous pos
            {
                start = i + 1;
                currentTank = 0;
            }
        }

        return totalDiff >= 0? start : -1;
    }
}