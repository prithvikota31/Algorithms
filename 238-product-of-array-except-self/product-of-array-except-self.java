class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] forwardProduct = new int[n];

        forwardProduct[0] = nums[0];
        //1 2 3 4
        //fp = 1 2 6 24
        for(int i = 1; i < n; i++)
        {
            forwardProduct[i] = forwardProduct[i - 1] * nums[i];
        }

        int[] ans = new int[n];
        int backwardproduct = nums[n - 1]; //4
        ans[n - 1] = forwardProduct[n - 2]; //     ,6
        for(int i = n - 2; i > 0; i--)
        {
            ans[i] = forwardProduct[i - 1] * backwardproduct;
            backwardproduct =  backwardproduct * nums[i];
        }
        ans[0] = backwardproduct;

        return ans;
    }
}