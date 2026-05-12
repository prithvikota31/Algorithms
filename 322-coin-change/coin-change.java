class Solution {
    public int coinChange(int[] coins, int amount) {
        //dp[i][j] => till ith index of coins, minimum coins to make amount j;

        int[][] dp = new int[coins.length][amount + 1];

        for(int i = 0; i < coins.length; i++)
        {
            Arrays.fill(dp[i], (int)1e9);
        }

        //base condition if i = 0, only 1st coin, check if remainder is zero

        for(int j = 0; j <= amount; j++)
        {
            if(j % coins[0] == 0)
            {
                dp[0][j] = j / coins[0];
            }
        }

        for(int i = 1; i < coins.length; i++)
        {
            for(int j = 0; j <= amount; j++)
            {
                int take = (int)1e9;
                if(coins[i] <= j)
                {
                    take = 1 + dp[i][j - coins[i]];
                }
                int notTake = dp[i - 1][j];
                dp[i][j] = Math.min(take, notTake);
            }
        }

        if(dp[coins.length - 1][amount] >= (int)1e9)    return -1;
        return dp[coins.length - 1][amount];
    }
}