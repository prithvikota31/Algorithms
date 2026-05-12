class Solution {

    public int coinChange(int[] coins, int amount) {

        Integer[][] dp = new Integer[coins.length][amount + 1];

        int ans = countCoins(coins, amount, 0, dp);

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    private int countCoins(int[] coins, int amount,
                           int ind, Integer[][] dp) {

        if(amount == 0) {
            return 0;
        }

        if(amount < 0 || ind == coins.length) {
            return Integer.MAX_VALUE;
        }

        if(dp[ind][amount] != null) {
            return dp[ind][amount];
        }

        int take = countCoins(coins,
                              amount - coins[ind],
                              ind,
                              dp);

        if(take != Integer.MAX_VALUE) {
            take = 1 + take;
        }

        int notTake = countCoins(coins,
                                 amount,
                                 ind + 1,
                                 dp);

        return dp[ind][amount] = Math.min(take, notTake);
    }
}