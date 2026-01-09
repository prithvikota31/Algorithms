class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        //brute
        //Input: gas =  [1, 2, 3, 4, 5]
        //        cost = 3  4  5  1  2
         // i=            0  1  2  3  4   

        // i = 3 -> 0 + 4 - 1 =  
        int n = gas.length;
        // try for all indexes as it start
        int i = 0;
        while(i < n)
        {
            int start = i;
            int fuelLeft = 0;
            int stationCount = 0;
            while(fuelLeft + gas[start] - cost[start] >= 0)  //it can go to next
            {
                fuelLeft = fuelLeft + gas[start] - cost[start];
                start = (start + 1) % n;
                stationCount++;
                if(stationCount == n)  return i;
            }

            i = (i + stationCount + 1);
        }

        return -1;


    }
}