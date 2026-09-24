class Solution {
    public List<List<Integer>> threeSum(int[] nums) {

        //make num sorted
        Arrays.sort(nums);
        //3 Sum
        //fix first value
        //then sweep across, from first + 1 to end to remaining two values
        List<List<Integer>> result = new ArrayList<>();
        // eg: 0, 1, 2, 3, 4, 5
        for(int i = 0; i <= nums.length - 3; i++)
        {
            if(i != 0 && nums[i] == nums[i - 1])
            {
                continue;
            }
            int j = i + 1;
            int k = nums.length - 1;
            while(j < k)
            {
                int sum = nums[i] + nums[j] + nums[k];
                if(j != i + 1 && nums[j] == nums[j - 1])
                {
                    j++;
                    continue;
                }
                if(sum == 0)
                {
                    result.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    j++;
                    k--;
                }
                else if(sum < 0)
                {
                    j++;
                }
                else
                {
                    k--;
                }
            }
        }
        return result;

    }
}