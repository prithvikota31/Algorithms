class Solution {
public double findMedianSortedArrays(int[] nums1, int[] nums2) {

    int n1 = nums1.length;
    int n2 = nums2.length;
    int n = n1 + n2;

    // always binary search on smaller array
    // to keep partition valid and avoid out-of-bounds
    if (n1 > n2) {
        return findMedianSortedArrays(nums2, nums1);
    }

    // total elements required on left partition
    // left side gets one extra element when total length is odd
    int left = (n + 1) / 2;

    int low = 0;
    int high = n1;

    while (low <= high) {

        // number of elements taken from nums1 into left partition
        int mid1 = low + (high - low) / 2;

        // remaining elements needed from nums2
        int mid2 = left - mid1;

        // sentinel values for partition edges
        int l1 = Integer.MIN_VALUE;
        int l2 = Integer.MIN_VALUE;

        int r1 = Integer.MAX_VALUE;
        int r2 = Integer.MAX_VALUE;

        // first element on right side from nums1
        if (mid1 < n1) {
            r1 = nums1[mid1];
        }

        // first element on right side from nums2
        if (mid2 < n2) {
            r2 = nums2[mid2];
        }

        // last element on left side from nums1
        if (mid1 - 1 >= 0) {
            l1 = nums1[mid1 - 1];
        }

        // last element on left side from nums2
        if (mid2 - 1 >= 0) {
            l2 = nums2[mid2 - 1];
        }

        // valid partition:
        // all left elements <= all right elements
        if (l1 <= r2 && l2 <= r1) {

            // even total length:
            // median is average of middle two elements
            if (n % 2 == 0) {

                int leftMedian = Math.max(l1, l2);
                int rightMedian = Math.min(r1, r2);

                return (double) (leftMedian + rightMedian) / 2.0;
            }

            // odd total length:
            // median is max element on left partition
            else {
                return (double) Math.max(l1, l2);
            }
        }

        // too many elements taken from nums1
        // move partition left
        else if (l1 > r2) {
            high = mid1 - 1;
        }

        // too few elements taken from nums1
        // move partition right
        else {
            low = mid1 + 1;
        }
    }

    return 0;
}
}