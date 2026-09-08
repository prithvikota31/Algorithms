class Solution {
    public int rob(int[] nums) {
        int n = nums.length;
        if(n <= 1)
        {
            return nums[0];
        }

        int first = robHelper(nums, 0, n - 2);
        int second = robHelper(nums, 1, n - 1);

        return Math.max(first, second);
    }

    public int robHelper(int[] nums, int start, int end) {
        if (start == end) return nums[start];
        int n = nums.length;

        int[] dp = new int[n];

        dp[start] = nums[start];

        dp[start + 1] = Math.max(nums[start], nums[start + 1]);

        for(int i = start + 2; i <= end; i++)
        {
            dp[i] = Math.max(dp[i-2] + nums[i], dp[i-1]);
        }

        return dp[end];
    }
}