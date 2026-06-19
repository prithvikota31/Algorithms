class Solution {
    public int coinChange(int[] coins, int amount) {
        int[][] dp = new int[coins.length][amount + 1];

        //dp[i][j] represent, coins required to make j amount with till ith index coins
        int m = dp.length;
        int n = dp[0].length;
        for(int i = 0; i < m; i++)
        {
            Arrays.fill(dp[i], (int)1e9);
        }

        for(int i = 0; i < m; i++)
        {
            dp[i][0] = 0;
        }

        for(int j = 0; j < n; j++)
        {
            if((j % coins[0]) == 0)
            {
                dp[0][j] = (j / coins[0]);
            }
        }


        for(int i = 1; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                //take this coin and not take this coin
                //nottake
                int notTake = dp[i - 1][j];
                //take 
                int take = (int)1e9;
                if(j >= coins[i])
                {
                    take = 1 + dp[i][j - coins[i]];
                }

                dp[i][j] = Math.min(take, notTake);
            }
        }
        return dp[m - 1][n - 1] == (int)1e9? -1: dp[m - 1][n - 1];
    }
}