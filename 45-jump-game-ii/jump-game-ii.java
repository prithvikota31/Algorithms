class Solution {
    public int jump(int[] nums) {
        int minJumps = 0;
        int start = 0;
        int end = 0;
        int maxPossibleJump = 0;
        for(int i = 0; i < nums.length; i++)
        {   
            if(i > end)
            {
               minJumps++;
               start = i;
               end = maxPossibleJump;
            }
            maxPossibleJump = Math.max(maxPossibleJump, i + nums[i]);
        }

        return minJumps;
    }
}