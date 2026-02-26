class Solution {
    public int lengthOfLIS(int[] nums) {
        //dp[i] denotes length till i

        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = 1;

        for(int i = 1; i < n; i++)
        {
            dp[i] = 1;
        }
        int max = 1;

        for(int i = 1; i < n; i++)
        {
            //check ith elemnt(nums[i]) to all elemetns before it
            for(int j = 0; j < i; j++)
            {
                if(nums[i] > nums[j])
                {
                    dp[i] = Math.max(dp[j] + 1, dp[i]);
                } 
            }
            max = Math.max(max, dp[i]);
        }

        return max;

    }
}