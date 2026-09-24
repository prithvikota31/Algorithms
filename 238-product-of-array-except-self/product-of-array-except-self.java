class Solution {
    public int[] productExceptSelf(int[] nums) {
        //1 2 3 4
        //first calculate prefix product for each pos
        //then calculate suffix product and multiply with ans

        int n = nums.length;
        int[] ans = new int[n];

        int prefix = 1;
        for(int i = 0; i < nums.length; i++)
        {
            ans[i] = prefix;
            prefix *= nums[i];
        }

        int suffix = 1;
        for(int i = n - 1; i >= 0; i--)
        {
            ans[i] *= suffix;
            suffix *= nums[i];
        }

        return ans;
    }
}