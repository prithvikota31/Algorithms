class Solution {
    public int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        //2, 7, 11, 15
        for(int i = 0; i < nums.length; i++)
        {
            int find = target - nums[i];

            if(map.containsKey(find))
            {
                return new int[]{map.get(find), i};
            }
            else
            {
                map.put(nums[i], i);
            }
        }

        return new int[]{-1, -1};
    }
}