class Solution {
    public int jump(int[] nums) {
        int l = 0, r = 0, jumps = 0;
        int farthest = 0;
        while(r < nums.length - 1)
        {
            for(int i = l; i <= r; i++)
            {
                farthest = Math.max(farthest, i + nums[i]);
            }

            jumps++;
            l = r + 1;
            r = farthest;
        }

        return jumps;
    }
}