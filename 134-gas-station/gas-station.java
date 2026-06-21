class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int totalGas = 0;

        int availableGas = 0;
        int start = 0;
        for(int i = 0; i < gas.length; i++)
        {
            availableGas += gas[i] - cost[i];
            totalGas += gas[i] - cost[i];
            if(availableGas < 0)
            {
                start = i + 1;
                availableGas = 0;
            }
        }

        if(totalGas < 0)    return -1;
        else
        {
            return start;
        }

    }
}