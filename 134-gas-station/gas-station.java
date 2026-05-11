class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int availableGas = 0;
        int netGas = 0;

        for(int i = 0; i < gas.length; i++)
        {
            netGas += gas[i] - cost[i];
        }
        if(netGas < 0)  return -1;

        int start = 0;
        for(int i = 0; i < gas.length; i++)
        {
            availableGas += gas[i] - cost[i];
            if(availableGas < 0)
            {
                availableGas = 0;
                start = i + 1;
            }
        }


        return start;
    }
}