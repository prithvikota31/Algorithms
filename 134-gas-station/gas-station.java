class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        //at any station net gas to go to next station
        //availableGas + gas[i] - cost[i] to go next station
        // if above value is positive it can go, otherwise break

        //check net gas +ve or not, otherwise it is not possible
        //calculate this from 0th index to last, at any time, net becomes -ve, we can skip gasStations till that point


        int availableGas = 0;
        int net = 0;
        int startIndex = 0;
        for(int i = 0; i < gas.length; i++)
        {
            availableGas = availableGas + gas[i] - cost[i];

            net += (gas[i] - cost[i]);

            if(availableGas < 0)
            {
                availableGas = 0;
                startIndex = i + 1;
            }
        }

        return net >= 0? startIndex: -1;

    }
}