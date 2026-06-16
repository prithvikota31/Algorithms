class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;

        int[] ans = new int[n];
        //first left product
        ans[0] = 1; //since no left elements

        for(int i = 1; i < n; i++)
        {
            ans[i] = nums[i - 1] * ans[i - 1];
        }

        //now capture right prodict

        //ans[n - 1], leave as it is
        int rightProduct = nums[n - 1];
        for(int i = n - 2; i >= 0; i--)
        {
            ans[i] = ans[i] * rightProduct;
            rightProduct = rightProduct * nums[i];
        }

        return ans;
    }
}