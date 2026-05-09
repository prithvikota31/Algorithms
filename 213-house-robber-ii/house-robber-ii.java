class Solution {
    public int rob(int[] nums) {
        int n = nums.length;
        if(n == 1)  return nums[0];
        int case1 = robLinear(nums, 0, n - 2);
        int case2 = robLinear(nums, 1, n - 1);

        return Math.max(case1, case2);
    }

    public int robLinear(int[] nums, int start, int end)
    {
        int n = end - start + 1;
        int[] dp = new int[nums.length];
        if(n == 1)    return nums[start];
        dp[start] = nums[start];
        dp[start + 1] = Math.max(nums[start + 1], nums[start]);
        for(int i = start + 2; i < nums.length; i++)
        {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }

        return dp[end];
    }
}