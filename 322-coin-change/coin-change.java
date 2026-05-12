class Solution {

    public int coinChange(int[] coins, int amount) {

        if(amount == 0) return 0;

        int[] result = new int[]{Integer.MAX_VALUE};

        Integer[][] dp = new Integer[coins.length][amount + 1];

        countCoins(coins, amount, 0, result, 0, dp);

        return result[0] == Integer.MAX_VALUE ? -1 : result[0];
    }

    private void countCoins(int[] coins,
                            int amount,
                            int ind,
                            int[] result,
                            int count,
                            Integer[][] dp) {

        if(amount < 0) {
            return;
        }

        if(amount == 0) {
            result[0] = Math.min(result[0], count);
            return;
        }

        if(ind == coins.length) {
            return;
        }

        // already visited with better/same count
        if(dp[ind][amount] != null &&
           dp[ind][amount] <= count) {
            return;
        }

        dp[ind][amount] = count;

        int curCoin = coins[ind];

        // take
        countCoins(coins,
                   amount - curCoin,
                   ind,
                   result,
                   count + 1,
                   dp);

        // not take
        countCoins(coins,
                   amount,
                   ind + 1,
                   result,
                   count,
                   dp);
    }
}