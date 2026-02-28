class Solution {
    public int findTargetSumWays(int[] nums, int target) {

        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // Invalid cases: cannot partition
        if ((totalSum + target) % 2 != 0 || Math.abs(target) > totalSum) {
            return 0;
        }

        int newTarget = (totalSum + target) / 2;

        // dp[j] = number of ways to get sum j
        int[] dp = new int[newTarget + 1];

        // Base case: one way to make sum 0
        dp[0] = 1;

        for (int num : nums) {
            // Traverse backwards to avoid reuse in same iteration
            for (int j = newTarget; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }

        return dp[newTarget];
    }
}