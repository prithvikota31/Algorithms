class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int totalGas = 0;

        int availableGas = 0;
        int start = 0;
        for(int i = 0; i < gas.length; i++)
        {
            int diff = gas[i] - cost[i];
            availableGas += diff;
            totalGas += diff;
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