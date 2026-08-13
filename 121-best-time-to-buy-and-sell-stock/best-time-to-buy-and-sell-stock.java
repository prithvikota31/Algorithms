class Solution {
    public int maxProfit(int[] prices) {
        int max = 0;
        int slidingMin = prices[0];
        for(int i = 1; i < prices.length; i++)
        {
            max = Math.max(max, prices[i] - slidingMin);
            slidingMin = Math.min(slidingMin, prices[i]);
        }

        return max;
    }
}