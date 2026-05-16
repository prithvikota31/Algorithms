class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        //sum
        //s1 - s2 = target
        //s1 + s2 = sum

        //s1 = target + sum / 2
        int sum = 0;
        for(int num: nums)
        {
            sum += num;
        }

        if((target + sum) % 2 !=  0 || sum < Math.abs(target))
        {
            return 0;
        }
        int newTarget = (target + sum) / 2;
        int m = nums.length;
        int n = newTarget;

        int[][] dp = new int[m + 1][n + 1];

        //0th row implies no elements;
        dp[0][0] = 1; //no elemeents target 0

        for(int i = 1; i < dp.length; i++)
        {
            for(int j = 0; j < dp[0].length; j++)
            {
                //dont use current element
                //notInclude
                dp[i][j]  += dp[i - 1][j];
                //include
                if(j >= nums[i - 1])
                    dp[i][j] += dp[i - 1][j - nums[i - 1]]; 
            }
        }

        return dp[m][n];



    }
}