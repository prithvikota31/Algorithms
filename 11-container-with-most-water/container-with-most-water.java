class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;

        int maxArea = Integer.MIN_VALUE;
        while(left < right)
        {
            if(height[left] <= height[right])
            {
                int width = right - left;
                maxArea = Math.max(maxArea, height[left] * width);
                left++;
            }
            else
            {
                int width = right - left;
                maxArea = Math.max(maxArea, height[right] * width);
                right--;
            }
        }

        return maxArea;
    }
}