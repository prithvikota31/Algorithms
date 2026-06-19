class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }

        // We need s1 - s2 = target and s1 + s2 = sum.
        // So s1 = (target + sum) / 2.
        if ((target + sum) % 2 != 0 || sum < Math.abs(target)) {
            return 0;
        }

        int subsetTarget = (target + sum) / 2;
        return countSubsets(nums, subsetTarget);
    }

    private int countSubsets(int[] nums, int target) {
        int m = nums.length;
        int[][] dp = new int[m + 1][target + 1];

        // With 0 elements, there is exactly 1 way to make sum 0: choose nothing.
        dp[0][0] = 1;

        // dp[i][j] = number of ways to make sum j using first i elements.
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j <= target; j++) {

                // Do not use current element nums[i - 1].
                dp[i][j] += dp[i - 1][j];

                // Use current element nums[i - 1], if it fits.
                if (j >= nums[i - 1]) {
                    dp[i][j] += dp[i - 1][j - nums[i - 1]];
                }
            }
        }

        return dp[m][target];
    }
}