class Solution {
    public boolean canJump(int[] nums) {
        int maxJump = 0;

        //each 
        for(int i = 0; i < nums.length; i++)
        {
            if(i > maxJump) return false;
            maxJump = Math.max(i + nums[i], maxJump);
        }

        return maxJump >= nums.length - 1? true: false;
    }
}