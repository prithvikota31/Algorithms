class Solution {
    public int coinChange(int[] coins, int amount) {
        int m = coins.length;
        int n = amount + 1;
        int[][] dp = new int[m][n];
        int INF = (int) 1e9;

        for(int i = 0; i < m; i++)
        {
            Arrays.fill(dp[i], INF);
        }

        //amount 0 needs no coins, first column 0
        //fill first row, with only one coin

        for(int i = 0; i < m; i++)
        {
            dp[i][0] = 0;
        }

        for(int totalAmount = 1; totalAmount <= amount; totalAmount++)
        {
            if(totalAmount % coins[0] == 0)
            {
                dp[0][totalAmount] = totalAmount / coins[0];
            }
        }


        for(int i = 1; i < coins.length; i++)
        {
            for(int totalAmount = 1; totalAmount <= amount; totalAmount++)
            {
                //dp[i][totalAmount]
                //1 + dp[i][totalAmount - coins[i]]
                //dp[i - 1][totalAmount]
                int take = INF; //take ith coin
                if(totalAmount >= coins[i])
                {
                    take = 1 + dp[i][totalAmount - coins[i]];
                }
                int notTake = dp[i - 1][totalAmount];

                dp[i][totalAmount] = Math.min(take, notTake);
            }
        }

        return dp[m- 1][n -1] == INF? -1: dp[m - 1][n - 1];
    }
}