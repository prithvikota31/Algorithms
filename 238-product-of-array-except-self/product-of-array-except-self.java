class Solution {
    public int[] productExceptSelf(int[] nums) {
        
        int n = nums.length;
        int[] ans = new int[n];

        ans[n - 1] = 1; //first capture right product

        for(int i = n - 2; i >= 0; i--)
        {
            ans[i] = nums[i + 1] * ans[i + 1];
        } 

        // int[] leftProduct = new int[n];

        // leftProduct[0] = 1;

        int leftProduct = 1;
        //now capture left product
        for(int i = 1; i <= n -1; i++)
        {
            ans[i] *= leftProduct * nums[i - 1]; 
            leftProduct *= nums[i - 1];
        }

        // int[] ans = new int[n];

        // for(int i = 0; i < n; i++)
        // {
        //     ans[i] = leftProduct[i] * rightProduct[i];
        // }

        return ans;

    }
}