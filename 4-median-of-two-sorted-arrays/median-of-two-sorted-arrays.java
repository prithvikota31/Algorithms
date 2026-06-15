class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;

        if(n1 > n2)
        {
            return findMedianSortedArrays(nums2, nums1);
        }

        int leftCount = (n1 + n2 + 1) / 2;
        //divide two arrays into left and right
        // left elemetns can range from 0 to all
        int left = 0;
        int right = n1;

        while(left <= right)
        {
            int mid1 = right + (left - right) / 2; // 0 to mid1 - 1
            int mid2 = leftCount - mid1; // o t0 mid2 - 1;

            //l1, l2, r1, r2
            int l1 = Integer.MIN_VALUE;
            int l2 = Integer.MIN_VALUE;
            int r1 = Integer.MAX_VALUE;
            int r2 = Integer.MAX_VALUE;

            if(mid1 < n1)
            {
                r1 = nums1[mid1];
            }
            if(mid2 < n2)
            {
                r2 = nums2[mid2];
            }

            if(mid1 - 1 >= 0)
            {
                l1 = nums1[mid1 - 1];
            }
            if(mid2 - 1 >= 0)
            {
                l2 = nums2[mid2 - 1];
            }

            //by default l1 <= r1 and l2 <= r2
            if(l1 <= r2 && l2 <= r1) //found answer
            {
                int totalElements = n1 + n2;
                if(totalElements % 2 == 0)
                {
                    return ((double)Math.max(l1, l2) + Math.min(r1, r2)) / 2.0;
                }
                else
                {
                    return (double)Math.max(l1, l2);
                }
            }
            else if(l1 > r2)
            {
                right = mid1 - 1;
            }
            else
            {
                left = mid1 + 1;
            }

        }

        return 0;

    }
}