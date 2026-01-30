class Solution {
    public int maxArea(int[] height) {
        // area = min(height(l),height(r)) * (r - l);
        int maximumArea = Integer.MIN_VALUE;
        int l = 0;
        int r = height.length - 1;

        while(l < r)
        {
            int curArea = Math.min(height[l], height[r]) * (r - l);
            maximumArea = Math.max(maximumArea, curArea);

            if(height[l] >= height[r])    r--;
            else l++;

        }

        return maximumArea;
    }
}