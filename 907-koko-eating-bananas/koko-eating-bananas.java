class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        //piles.length <= h, so always possible
        //max hours is piles.length
        //we need to find proper k between range, 1 to max
        int low = 1;
        int high = 0;

        for(int p: piles)
        {
            high = Math.max(high, p);
        }

        int k = high;
        while(low <= high)
        {
            int mid = low + (high - low) / 2;

            //calculate hours using mid speed
            long hrs = 0;
            for(int pile : piles)
            {
                hrs += (pile + mid - 1) / mid;
            }

            if(hrs > h)
            {
                low = mid + 1;
            }
            else
            {
                k = Math.min(k, mid);
                high = mid - 1;
            }
        }
        return k;

    }
}