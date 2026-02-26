class Solution {
    public int findDuplicate(int[] nums) {
        int slow = nums[0];
        int fast = nums[nums[0]]; // so starting common point is zero

        while(slow != fast)
        {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } // meeting point

        slow = 0;
        while(slow != fast)
        {
            slow = nums[slow];
            fast = nums[fast];
        }

        return slow;
    }
}