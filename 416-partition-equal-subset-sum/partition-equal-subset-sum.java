class Solution {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for(int num: nums)
        {
            totalSum += num;
        }

        if(totalSum % 2 != 0)   return false;

        return targetSum(nums, totalSum / 2);
    }

    private boolean targetSum(int[] nums, int target)
    {
        boolean[][] dp = new boolean[nums.length][target + 1];

        int m = dp.length;
        int n = dp[0].length;

        for(int i = 0; i < m; i++)
        {
            dp[i][0] = true;
        }

        //0th row mark only target column

        if(target >= nums[0])
        {
            dp[0][nums[0]] = true;
        }

        for(int i = 1; i < m; i++)
        {
            for(int j = 1; j < n; j++)
            {
                boolean notInclude = dp[i - 1][j];

                boolean include = false;
                if(j >= nums[i])
                {
                    include = dp[i - 1][j - nums[i]];
                }

                dp[i][j] = include || notInclude;
            }
        }

        return dp[m - 1][n - 1];

    }
}