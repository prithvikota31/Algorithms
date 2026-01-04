class Solution {
    public int coinChange(int[] coins, int amount) {
        //recursion idea
        //f(ind, amount) 
        // when last index is taken => f(ind, amount - coins[index])
        //not taken => f(ind - 1, amount)
        // base: if(ind == 0) => either amount/arr[0] or Integer.MAX_VALUE;
        //dp[i][j]
        int m = coins.length;
        int n = amount + 1;

        int[][] dp = new int[m][n];

        //fill row with first index (i.e using only first value in coins)
        for(int j = 0; j <= amount; j++)
        {
            if(j % coins[0] == 0)   dp[0][j] = j / coins[0];
            else
                dp[0][j] = (int)1e9;
        }

        for(int i = 0; i < coins.length; i++)
        {
            dp[i][0] = 0;
        }

        for(int i = 1; i < coins.length; i++)
        {
            for(int j = 1; j <= amount; j++)
            {
                int notTake = dp[i - 1][j];
                int take = (int)1e9;
                if(j - coins[i] >= 0) take = 1 + dp[i][j - coins[i]];
                dp[i][j] = Math.min(notTake, take);
            }
        }
        return dp[m - 1][n - 1] >= 1e9? -1 : dp[m - 1][n - 1];
    }
}