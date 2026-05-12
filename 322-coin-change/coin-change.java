class Solution {
    public int coinChange(int[] coins, int amount) {

        int[] dp = new int[amount + 1];

        // Fill with impossible large value
        Arrays.fill(dp, amount + 1);

        // Base case
        dp[0] = 0;

        // Build answers from 1 -> amount
        for (int i = 1; i <= amount; i++) {

            // Try every coin
            for (int coin : coins) {

                // Coin can participate
                if (coin <= i) {

                    dp[i] = Math.min(
                        dp[i],
                        1 + dp[i - coin]
                    );
                }
            }
        }

        // Impossible case
        return dp[amount] > amount ? -1 : dp[amount];
    }
}