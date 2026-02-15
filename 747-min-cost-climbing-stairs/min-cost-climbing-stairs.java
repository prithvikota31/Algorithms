class Solution {
    public int minCostClimbingStairs(int[] cost) {
        //min length of cost = 2
        int[] dp = new int[cost.length];
        dp[0] = 0; //cost to reach index 0
        dp[1] = 0; //cost to reach index 1

        for(int i = 2; i < cost.length; i++)
        {
            dp[i] = Math.min(cost[i - 1] + dp[i - 1], cost[i - 2] + dp[i - 2]);
        }


        return Math.min(dp[cost.length - 1] + cost[cost.length - 1], dp[cost.length - 2] + cost[cost.length - 2]);        
    }
}