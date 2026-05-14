class Solution {
    public int maxProfit(int[] prices) {
        int maximumProfit = 0;

        int min = prices[0];
        for(int i = 1; i < prices.length; i++)
        {
            min = Math.min(prices[i], min);
            maximumProfit = Math.max(maximumProfit, prices[i] - min);
            
        }

        return maximumProfit;
    }
}