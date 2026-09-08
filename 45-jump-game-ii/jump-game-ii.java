class Solution {
    public int jump(int[] nums) {
        int minJumps = 0;

        int start = 0;
        int end = 0;
        int reach = 0;
        for(int i = 0; i < nums.length - 1; i++)
        {
            reach = Math.max(reach, i + nums[i]);
            if(i == end)
            {
                end = reach;
                minJumps++;
            }   
        }
        return minJumps;
    }
}