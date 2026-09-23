class Solution {
    public boolean canPartition(int[] nums) {
        int sum = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }

        if (sum % 2 != 0) {
            return false;
        }

        int target = sum / 2;

        return targetSum(nums, target);
    }

    private boolean targetSum(int[] nums, int target) {
        // dp[i] represents whether sum i is possible
        int n = nums.length;
        boolean[] dp = new boolean[target + 1];

        dp[0] = true;

        // Process each number once
        for (int j = 0; j < n; j++) {

            // Go backward so nums[j] is not reused in the same iteration
            for (int i = target; i >= nums[j]; i--) {
                dp[i] = dp[i] || dp[i - nums[j]];
            }
        }

        return dp[target];
    }
}