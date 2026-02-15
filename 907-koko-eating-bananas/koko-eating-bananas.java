class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        //oiles.length <= h
        //find upperLimit k

        int upperK = Integer.MIN_VALUE;
        for(int pile: piles)
        {
            upperK = Math.max(upperK, pile);
        }
        //do binary search to find min K
        int low = 1;
        int high = upperK;
        int ans = Integer.MAX_VALUE;
        while(low <= high)
        {
            int mid = low + (high - low) / 2;
            if(calculateTime(piles, mid) <= (long)h)
            {
                ans = mid;
                high = mid - 1;
            }
            else
            {
                low = mid + 1;
            }
        }

        return ans;

    }


    private long calculateTime(int[] piles, int k) {
        long total = 0;
        for (int pile : piles) {
            total += (pile + (long)k - 1) / k;  // integer ceil, no double
        }
        return total;
    }



}