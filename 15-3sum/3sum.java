class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        Arrays.sort(nums);
        // 1, 1, 2, 2, 3, 4, 5, 6
        for(int i = 0; i <= n - 3; i++)
        {
            if(i > 0 && nums[i] == nums[i - 1])  continue;
            int j = i + 1;
            int k = n - 1;

            while(j < k)
            {             
                int sum = nums[i] + nums[j] + nums[k];
                if(sum > 0 )
                {
                    k--;
                }
                else if(sum < 0)
                {
                    j++;
                }
                else
                {
                    result.add(new ArrayList<>(Arrays.asList(nums[i], nums[j], nums[k])));
                    j++;
                    k--;

                    while(j > i + 1 && j < k && nums[j] == nums[j - 1])  
                    {
                        j++;
                    }
                
                    while(k < n - 1 && j < k && nums[k] == nums[k + 1])  
                    {
                        k--;
                    }
                }
            }
        }
        return result;

    }
}