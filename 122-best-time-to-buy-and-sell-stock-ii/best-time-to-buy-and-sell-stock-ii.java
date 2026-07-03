class Solution {
    public int maxProfit(int[] arr) {

        // Edge case: If no prices available, return 0
        int n = arr.length;
        if (n == 0) return 0;

        int[] prev = new int[2];

        // Iterate from the end of the array towards the beginning
        for (int ind = n - 1; ind >= 0; ind--) {
            int[] cur = new int[2];
            for (int buy = 0; buy <= 1; buy++) {
                int profit;
                if (buy == 0) {
                    // We can buy → two options:
                    // 1. Skip buying → profit remains dp[ind+1][0]
                    // 2. Buy → pay the price (-arr[ind]) and move to selling state
                    profit = Math.max(
                        0 + prev[0],
                        -arr[ind] + prev[1]
                    );
                } else {
                    // We can sell → two options:
                    // 1. Skip selling → profit remains dp[ind+1][1]
                    // 2. Sell → gain price and move to buying state
                    profit = Math.max(
                        0 + prev[1],
                        arr[ind] + prev[0]
                    );
                }
                cur[buy] = profit;
            }
            prev = cur;
        }

        // Final result is starting from index 0 with buying allowed
        return prev[0];
    }

    }
