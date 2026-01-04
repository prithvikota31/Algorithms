class Solution {
    public int findMin(int[] nums) {
        int ans = Integer.MAX_VALUE;

        int low = 0;
        int high = nums.length - 1;

        while(low <= high)
        {
            int mid = low + (high - low) /2;
            ans = Math.min(ans, nums[mid]);

            if(nums[mid] > nums[high])  //left side sorted
            {
                low = mid + 1;
            }
            else //right side sorted
            {
                high = mid - 1;
            }
        }

        return ans;
    }
}