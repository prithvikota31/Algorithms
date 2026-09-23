class Solution {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;

        List<Integer> temp = new ArrayList<>();

        for(int i = 0; i < nums.length; i++)
        {
            //check if nums[i] can be replaced with a value > nums[i] (just greate than value)
            int nextGreaterIndex = findJustGreater(temp, nums[i]);
            if(nextGreaterIndex == -1)
            {
                temp.add(nums[i]);
            }
            else
            {
                temp.set(nextGreaterIndex, nums[i]);
            }
        }

        return temp.size();
    }


    private int findJustGreater(List<Integer> temp, int val)
    {
        int index = -1;

        int low = 0;
        int high = temp.size() - 1;

        while(low <= high)
        {
            int mid = low + (high - low) / 2;

            if(temp.get(mid) >= val)
            {
                index = mid;
                high = mid - 1;
            }
            else
            {
                low = mid + 1;
            }
        }
        return index;
    }
}