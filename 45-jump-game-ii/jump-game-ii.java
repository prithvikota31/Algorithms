class Solution {
    public int jump(int[] nums) {

        int jumps = 0;

        // Current window end
        int end = 0;

        // Farthest reachable index
        int farthest = 0;

        for(int i = 0; i < nums.length - 1; i++) {

            // Expand reachable range
            farthest = Math.max(farthest, i + nums[i]);

            // End of current jump window
            if(i == end) {

                jumps++;

                // Move to next window
                end = farthest;
            }
        }

        return jumps;
    }
}