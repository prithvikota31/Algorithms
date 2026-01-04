class Solution {
    public int change(int amount, int[] coins) {
        //[1, 2, 5]
        //f(ind, amount)
        //

        int m = coins.length;
        int n = amount + 1;

        int[][] dp = new int[m][n];

        //fill row with first index (i.e using only first value in coins)
        for(int j = 0; j <= amount; j++)
        {
            if(j % coins[0] == 0)   dp[0][j] = 1;
            else
                dp[0][j] = 0;
        }

        for(int i = 0; i < coins.length; i++)
        {
            dp[i][0] = 1;
        }

        for(int i = 1; i < coins.length; i++)
        {
            for(int j = 1; j <= amount; j++)
            {
                int notTake = dp[i - 1][j];
                int take = 0;
                if(j - coins[i] >= 0) take = dp[i][j - coins[i]];
                dp[i][j] = (notTake + take);
            }
        }
        return dp[m - 1][n - 1];
    }
}