class Solution {
    public int change(int amount, int[] coins) {
        //f(index, amount)
        //    f(index, amount - coins[index])
        //    f(index - 1, amount)
        
        int[][] dp = new int[coins.length][amount + 1];
        int m = dp.length;
        int n = dp[0].length;

        for(int j = 0; j < n; j++)
        {
            if(j % coins[0] == 0)
            {
                dp[0][j] = 1;
            }
        }

        for(int i = 0; i < m; i++)
        {
            dp[i][0] = 1;
        }

        for(int i = 1; i < m; i++)
        {
            for(int j = 1; j < n; j++)
            {
                int take = 0;
                if(coins[i] <= j)
                {
                    take = dp[i][j - coins[i]];
                }
                int notTake = dp[i - 1][j];
                dp[i][j] = take + notTake;
            }
        }

        return dp[m - 1][n - 1];
    }   

    private int calculate(int index, int amount, int[] coins, int[][] dp)
    {
        if(index == 0)
        {
            if(amount % coins[index] == 0)
            {
                return 1;
            }
            else
            {
                return 0;   
            }
        }

        if(dp[index][amount] != -1) return dp[index][amount];

        //take
        int take = 0;
        if(amount >= coins[index])
        {
            take = calculate(index, amount - coins[index], coins, dp);
        }
        int notTake = calculate(index - 1, amount, coins, dp);

        return dp[index][amount] = take + notTake;
    }
}