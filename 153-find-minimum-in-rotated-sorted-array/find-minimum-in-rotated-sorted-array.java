class Solution {
    public int findMin(int[] nums) {
        //try moving away from sorted side
        int low = 0;
        int high = nums.length - 1;

        int min = Integer.MAX_VALUE;
        while(low <= high)
        {
            int mid = (low + high) / 2;

            
            if(nums[mid] < nums[high]) //right side sorted
            {
                min = Math.min(min, nums[mid]);
                high = mid - 1;
                
            }
            else
            {
                min = Math.min(min, nums[low]);
                low = mid + 1;
                
            }
        }

        return min;
    }
}