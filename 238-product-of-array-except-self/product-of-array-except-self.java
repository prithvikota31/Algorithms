class Solution {
    public int[] productExceptSelf(int[] nums) {
        
        //1, 2, 3, 4
        //leftproduct = product exisiting on left of number
        int n = nums.length;
        int[] leftProduct = new int[n];

        leftProduct[0] = 1; //since none to left og 0th index

        for(int i = 1; i < n; i++)
        {
            leftProduct[i] = leftProduct[i - 1] * nums[i - 1];
        }

        int rightProduct = 1;

        int[] result = new int[n];
        for(int i = n - 1; i >= 0; i--)
        {
            result[i] = rightProduct * leftProduct[i];
            rightProduct *= nums[i];
        }

        return result;
    }
}