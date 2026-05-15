class Solution {

    public int lengthOfLIS(int[] nums) {

        int[] tails = new int[nums.length];

        tails[0] = nums[0];

        int lisEndIndex = 0;

        for(int i = 1; i < nums.length; i++) {

            // Current number can extend LIS
            if(nums[i] > tails[lisEndIndex]) {

                tails[++lisEndIndex] = nums[i];
                continue;
            }

            // Find first element >= nums[i]
            int replaceIndex =
                lowerBound(tails, 0, lisEndIndex, nums[i]);

            // Replace with smaller tail
            tails[replaceIndex] = nums[i];
        }

        return lisEndIndex + 1;
    }

    public int lowerBound(
        int[] nums,
        int left,
        int right,
        int target
    ) {

        while(left <= right) {

            int mid = left + (right - left) / 2;

            if(nums[mid] >= target) {
                right = mid - 1;
            }
            else {
                left = mid + 1;
            }
        }

        return left;
    }
}