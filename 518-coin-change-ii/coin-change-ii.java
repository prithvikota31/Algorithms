class Solution {
    public int change(int amount, int[] coins) {
        //f(index, amount)
        //    f(index, amount - coins[index])
        //    f(index - 1, amount)
        
        int[][] dp = new int[coins.length][amount + 1];
        int m = dp.length;
        int n = dp[0].length;

        int[] cur = new int[n];
        int[] prev = new int[n];

        // for(int j = 0; j < n; j++)
        // {
        //     if(j % coins[0] == 0)
        //     {
        //         dp[0][j] = 1;
        //     }
        // }

        for(int j = 0; j < n; j++)
        {
            if(j % coins[0] == 0)
            {
                prev[j] = 1;
            }
        }

        // for(int i = 0; i < m; i++)
        // {
        //     dp[i][0] = 1;
        // }

        for(int i = 1; i < m; i++)
        {
            cur[0] = 1;
            for(int j = 1; j < n; j++)
            {
                int take = 0;
                if(coins[i] <= j)
                {
                    take = cur[j - coins[i]];
                }
                int notTake = prev[j];
                cur[j] = take + notTake;
            }

            prev = cur.clone();
        }

        return prev[n - 1];
    }   


}