class Solution {
    public int lengthOfLIS(int[] nums) {
        int[] target = new int[nums.length];
        target[0] = nums[0];
        int ind = 0;
        for(int i = 1; i < nums.length; i++)
        {
            if(nums[i] > target[ind])
            {
                target[++ind] = nums[i];
                continue;
            }
            int replaceIndex = firstNoGreater(target, 0, ind, nums[i]);
            target[replaceIndex] = nums[i];
        }

        return ind + 1;
    }

    public int firstNoGreater(int[] nums, int i, int j, int target)
    {
        int low = i;
        int high = j;
        int resultIndex = Integer.MAX_VALUE;
        while(low <= high)
        {
            int mid = (low + high) / 2;
            if(nums[mid] >= target)
            {    
                resultIndex = Math.min(resultIndex, mid);
                high = mid - 1;
            }
            else
            {
                low = mid + 1;
            }
        }

        return resultIndex;
    }
}