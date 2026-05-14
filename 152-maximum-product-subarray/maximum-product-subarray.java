class Solution {
    public int maxProduct(int[] nums) {

        int min = nums[0];
        int max = nums[0];
        int result = nums[0];

        for(int i = 1; i < nums.length; i++) {

            int curNum = nums[i];

            int prevMin = min;
            int prevMax = max;

            min = Math.min(
                    curNum,
                    Math.min(curNum * prevMin,
                             curNum * prevMax)
                  );

            max = Math.max(
                    curNum,
                    Math.max(curNum * prevMin,
                             curNum * prevMax)
                  );

            result = Math.max(result, max);
        }

        return result;
    }
}