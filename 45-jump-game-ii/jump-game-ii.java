class Solution {
    public int jump(int[] nums) {
        int count = 0;
        int farthest = 0;
        
        int end = 0;
        //2, 3, 1, 1, 4
        for(int i = 0; i < nums.length - 1; i++)
        {
            farthest = Math.max(farthest, i + nums[i]);
            if(i == end)
            {
                count++;
                end = farthest;
                if(end >= nums.length - 1)
                {
                    break;
                }
            }
        }

        return count;
    }
}