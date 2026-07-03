class Solution {
    public int maxProfit(int[] arr) {

        // Edge case: If no prices available, return 0
        int n = arr.length;
        if (n == 0) return 0;

        // DP table where dp[ind][buy] stores max profit from index 'ind' onwards
        // buy = 0 → can buy, buy = 1 → can sell
        long[][] dp = new long[n + 1][2];

        // Base condition: When at day n (beyond last), no profit can be made
        dp[n][0] = dp[n][1] = 0;

        // Iterate from the end of the array towards the beginning
        for (int ind = n - 1; ind >= 0; ind--) {
            for (int buy = 0; buy <= 1; buy++) {
                long profit;
                if (buy == 0) {
                    // We can buy → two options:
                    // 1. Skip buying → profit remains dp[ind+1][0]
                    // 2. Buy → pay the price (-arr[ind]) and move to selling state
                    profit = Math.max(
                        0 + dp[ind + 1][0],
                        -arr[ind] + dp[ind + 1][1]
                    );
                } else {
                    // We can sell → two options:
                    // 1. Skip selling → profit remains dp[ind+1][1]
                    // 2. Sell → gain price and move to buying state
                    profit = Math.max(
                        0 + dp[ind + 1][1],
                        arr[ind] + dp[ind + 1][0]
                    );
                }
                dp[ind][buy] = profit;
            }
        }

        // Final result is starting from index 0 with buying allowed
        return (int)dp[0][0];
    }

    }
