class Solution {
    public int maxProduct(int[] nums) {
        int prefixProduct = 1;
        int suffixProduct = 1;
        int maxProduct = Integer.MIN_VALUE;

        for(int i = 0; i < nums.length; i++)
        {
            prefixProduct = prefixProduct * nums[i];
            suffixProduct = suffixProduct * nums[nums.length - 1 - i];
            maxProduct = Math.max(maxProduct, Math.max(prefixProduct, suffixProduct));

            if(prefixProduct == 0)  prefixProduct = 1;
            if(suffixProduct == 0) suffixProduct = 1;
        }

        return maxProduct;
    }
}