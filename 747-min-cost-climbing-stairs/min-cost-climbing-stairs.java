class Solution {
    public int minCostClimbingStairs(int[] cost) {

        //top is last index + 1

        int top = cost.length;
        int[] dp = new int[cost.length];

        dp[0] = 0;
        dp[1] = 0;
        int minCost = 0;
        for(int i = 2; i < cost.length; i++)
        {
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }

        return Math.min(dp[top - 1] + cost[top - 1], dp[top - 2] + cost[top - 2]);


    }
}