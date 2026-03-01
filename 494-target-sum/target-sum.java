class Solution {
   public int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;

        // First calculate total sum of all numbers
        int totalSum = 0;
        for (int num : nums) totalSum += num;

        // If (target + totalSum) is odd or target > totalSum, no valid partition exists
        if ((totalSum + target) % 2 != 0 || Math.abs(target) > totalSum) return 0;

        // We now need to count subsets with sum = (target + totalSum) / 2
        int newTarget = (totalSum + target) / 2;

        // Create DP table: dp[i][j] = number of ways to make sum j using first i numbers
        int[][] dp = new int[n + 1][newTarget + 1];

        // Base case: One way to form sum 0 (by taking no elements)
        dp[0][0] = 1;
        // Fill DP table iteratively
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= newTarget; j++) {
                // Exclude current element
                dp[i][j] = dp[i - 1][j];

                // Include current element if it does not exceed current target j
                if (nums[i - 1] <= j) {
                    dp[i][j] += dp[i - 1][j - nums[i - 1]];
                }
            }
        }

        return dp[n][newTarget];
    }
}