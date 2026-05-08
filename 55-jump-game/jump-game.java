class Solution {
    public boolean canJump(int[] nums) {
        int maxCoverage = 0;
        int n = nums.length - 1;
        for(int i = 0; i <= n; i++)
        {
            if(i > maxCoverage) return false;
            maxCoverage = Math.max(maxCoverage, i + nums[i]);
            if(maxCoverage >= n)    return true;
        }

        return false;
    }
}