class Solution {
    public boolean canJump(int[] nums) {
        //[2,3,1,1,4]
        // 0 1 2 3 4
        //    At any position, it can reach uptil i + nums[i],
        //     so it is guaranteed that it will cover till that point 
        //carry a maxLimit to update at every index

        int maxLimit = 0;
        for(int i = 0; i < nums.length; i++)
        {
            if(i > maxLimit)    return false;
            maxLimit = Math.max(maxLimit, i + nums[i]);
        }

        return true;
    }
}