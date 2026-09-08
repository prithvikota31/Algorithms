class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        //h > piles length
        int high = Integer.MIN_VALUE;
        for(int pile: piles)
        {
            high = Math.max(high, pile);
        }
        int ans = high;

        int low = 1;
        while(low <= high)
        {
            int mid = low + (high - low) / 2;

            //check with mid if all piles are done
            boolean ableToFinish = canFinish(piles, h, mid);

            if(ableToFinish) //lets lower speed
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

    private boolean canFinish(int[] piles, int h, int speed)
    {
        long totalTime = 0;

        for(int pile: piles)
        {
            totalTime += (pile + speed - 1) / speed;
        }

        if((long)totalTime <= (long)h)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}