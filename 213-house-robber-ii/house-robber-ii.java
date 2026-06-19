class Solution {
    public int rob(int[] nums) {
        int n = nums.length;
        if(n <= 1)
        {
            return nums[0];
        }

        int firstHalf = robHelper(nums, 0, n - 2);
        int secondHalf = robHelper(nums, 1, n - 1);

        return Math.max(firstHalf, secondHalf);
    }

    public int robHelper(int[] nums, int start, int end) {
        int n = nums.length;
        int[] dp = new int[n];

        dp[start] = nums[start];
        if(end - start + 1 > 1)
        {
            dp[start + 1] = Math.max(nums[start], nums[start + 1]);
        }
        

        for(int i = start + 2; i <= end; i++)
        {
            dp[i] = Math.max(dp[i - 1], nums[i] + dp[i - 2]);
        }

        return dp[end];
    }
}