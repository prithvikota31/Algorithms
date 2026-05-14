class Solution {
    public int maxProduct(int[] nums) {
        int min = 1;
        int max = 1;
        int product = Integer.MIN_VALUE;


        for(int i = 0; i < nums.length; i++)
        {
            int curNum = nums[i];
            int prevMin = min;
            int prevMax = max;
            min = Math.min(curNum, Math.min(curNum * prevMin, curNum * prevMax));
            max = Math.max(curNum, Math.max(curNum * prevMin, curNum * prevMax));
            product = Math.max(max, product);
        }

        return product;
    }
}