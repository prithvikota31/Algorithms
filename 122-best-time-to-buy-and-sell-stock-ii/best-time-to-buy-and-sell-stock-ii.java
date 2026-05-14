class Solution {
    public int maxProfit(int[] prices) {
        int result = 0;
        int curStock = 0;
        for(int i = 0; i < prices.length; i++)
        {
            if(i > 0 && prices[i] > prices[i - 1])
            {
                result += prices[i] - prices[i - 1];
            }
        }

        return result;
    }
}