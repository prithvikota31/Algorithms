class Solution {
    public int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        // 9, 11, 16, 20, 0, 3, 4, 5

        while(low <= high)
        {
            int mid = low + (high - low) / 2;
            if(nums[mid] == target)  return mid;

            if(nums[mid] > nums[high]) //left side is sorted
            {
                if(target < nums[mid] && target >= nums[low])   high = mid - 1;
                else    low = mid + 1;
            }

            else // right side is sorted
            {
                if(target > nums[mid] && target <= nums[high])   low = mid + 1;
                else high = mid - 1;
            }
        }

        return -1;
    }
}