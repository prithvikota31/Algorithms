class Solution {
    public int jump(int[] nums) {
        // dp is a way, but grredy is better
        //2,3,1,1,4
        //0 1 2 3 4
        //..l...r
        // Maintain range after each jump;
        // by default l = 0, r = 0 for jump = 0;
        // 


        int l = 0, r = 0, jump = 0;

        while(r < nums.length - 1) //if r is already on n - 1 extra jump not needed
        {
            int nextJump = r + 1; //(minimum I can think of)
            for(int i = l; i <= r; i++)
            {
                nextJump = Math.max(nextJump, i + nums[i]);
            }
            l = r + 1;
            r = nextJump;
            jump++;
        }
        return jump;

    }
}