class Solution {
    public int maxProfit(int[] prices) {
        //f(ind, canBuy)
        //canBuy = 0, cannot buy
        int n = prices.length;
        int[][] dp = new int[n][2];
        for(int i = 0; i < dp.length; i++)
        {
            Arrays.fill(dp[i], -1);
        }

        return calculateProfit(0, 1, prices, n, dp);
    }

    private int calculateProfit(int index, int canBuy, int[] prices, int n, int[][] dp)
    {
        if(index >= n)
        {
            return 0;
        }
        int profit = 0;
        if(dp[index][canBuy] != -1) return dp[index][canBuy];
        if(canBuy == 1) //possible to buy
        {
            profit = Math.max(-prices[index] + calculateProfit(index + 1, 0, prices, n, dp),
                            0 + calculateProfit(index + 1, 1, prices, n, dp));
        }
        else
        {
            profit = Math.max(prices[index] + calculateProfit(index + 2, 1, prices, n, dp),
                            0 + calculateProfit(index + 1, 0, prices, n, dp));
        }

        return dp[index][canBuy] = profit;
    }
}