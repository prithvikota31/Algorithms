class Solution {
    public int findTargetSumWays(int[] nums, int target) {

        int n = nums.length;

        // Step 1: calculate total sum
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // Step 2: check invalid cases
        if ((totalSum + target) % 2 != 0 || Math.abs(target) > totalSum) {
            return 0;
        }

        int newTarget = (totalSum + target) / 2;

        // Step 3: create dp table
        // dp[i][j] = number of ways to form sum j using first i elements
        int[][] dp = new int[n + 1][newTarget + 1];

        // Step 4: base case
        dp[0][0] = 1;

        // Step 5: fill table
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= newTarget; j++) {

                // exclude current element
                dp[i][j] = dp[i - 1][j];

                // include current element
                if (nums[i - 1] <= j) {
                    dp[i][j] += dp[i - 1][j - nums[i - 1]];
                }
            }
        }

        return dp[n][newTarget];
    }
}