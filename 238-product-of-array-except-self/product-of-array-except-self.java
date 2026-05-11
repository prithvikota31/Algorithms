class Solution {
    public int[] productExceptSelf(int[] nums) {
        
        int n = nums.length;
        int[] rightProduct = new int[n];

        rightProduct[n - 1] = 1; //since nothing on right of last

        for(int i = n - 2; i >= 0; i--)
        {
            rightProduct[i] = nums[i + 1] * rightProduct[i + 1];
        } 

        int[] leftProduct = new int[n];

        leftProduct[0] = 1;

        for(int i = 1; i <= n -1; i++)
        {
            leftProduct[i] = leftProduct[i - 1] * nums[i - 1]; 
        }

        int[] ans = new int[n];

        for(int i = 0; i < n; i++)
        {
            ans[i] = leftProduct[i] * rightProduct[i];
        }

        return ans;

    }
}