class Solution {
    public int jump(int[] nums) {
        int n = nums.length;
        int count = 0;
        int maxJump = 0;
        int j = 0;

        for(int i = 0; i < n; i++)
        {
            if(j >= n - 1)  return count;

            if(i > j)
            {
                count++;
                j = maxJump;
            }

            maxJump = Math.max(maxJump, i + nums[i]);
        }


        return count;
    }
}