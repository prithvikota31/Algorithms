class Solution {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;

        for (int ind = 0; ind < n; ind++) {
            // Keep placing nums[ind] into its correct position.
            while (
                nums[ind] >= 1 &&
                nums[ind] <= n &&
                nums[nums[ind] - 1] != nums[ind]
            ) {
                swap(nums, ind, nums[ind] - 1);
            }
        }

        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        return n + 1;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}