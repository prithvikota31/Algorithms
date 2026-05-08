class Solution {
    public int maxSubArray(int[] nums) {
        //minimum size is 1(to consider subarray its not empty)

        int result = Integer.MIN_VALUE;
        int sum = 0;
        for(int i = 0; i < nums.length; i++)
        {
            sum += nums[i];
            result = Math.max(sum, result);
            if(sum < 0)
            {
                sum = 0;
            }  
        }
        return result;
    }
}