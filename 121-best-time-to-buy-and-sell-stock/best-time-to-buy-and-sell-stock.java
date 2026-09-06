class Solution {
    public int maxProfit(int[] prices) {
        int minimum = prices[0];
        int maxProfit = 0;
        for(int i = 1; i < prices.length; i++)
        {
            maxProfit = Math.max(maxProfit, prices[i] - minimum);
            minimum = Math.min(minimum, prices[i]);
        }

        return maxProfit;
    }
}