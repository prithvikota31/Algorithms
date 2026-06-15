class Solution {
    public int minEatingSpeed(int[] piles, int h) {

        int low = 1;
        int high = 0;

        // maximum possible useful speed
        for (int pile : piles) {
            high = Math.max(high, pile);
        }

        int answer = high;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            long totalHours = 0;

            // calculate total hours needed at speed = mid
            for (int pile : piles) {
                totalHours += (pile + mid - 1) / mid;
            }

            // speed too slow
            if (totalHours > h) {
                low = mid + 1;
            }
            // valid speed, try smaller speed
            else {
                answer = mid; 
                //answer = Math.min(answer, mid);
                high = mid - 1;
            }
        }

        return answer;
    }
}