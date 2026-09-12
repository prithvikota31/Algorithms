class Solution {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        List<Integer> temp = new ArrayList<>();


        for(int i = 0; i < n; i++)
        {
            int indexPosInTemp = greaterOrEqualValIndex(temp, nums[i]);
            if(indexPosInTemp == -1)
            {
                temp.add(nums[i]);
            }
            else
            {
                temp.set(indexPosInTemp, nums[i]);
            }
        }

        return temp.size();
    }


    // 1, 4, 5 , 8 , 10

    private int greaterOrEqualValIndex(List<Integer> temp, int target)
    {
        int index = -1;

        if(temp.size() == 0)
        {
            return index;
        }

        int low = 0;
        int high = temp.size() - 1;
        while(low <= high)
        {
            int mid = low + (high - low) / 2;

            if(temp.get(mid) >= target)
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