class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        //dp[i] denotes minium coins to reach amount i;
        Arrays.fill(dp, (int)1e9);

        dp[0] = 0;

        for(int curAmount = 1; curAmount <= amount; curAmount++)
        {
            for(int coinIndex = 0; coinIndex < coins.length; coinIndex++)
            {
                if(coins[coinIndex] <= curAmount)
                {
                    dp[curAmount] = Math.min(dp[curAmount],1 + dp[curAmount - coins[coinIndex]]);
                }
            }
        }

        if(dp[amount] >= 1e9)   return -1;
        else
            return dp[amount];
    }
}