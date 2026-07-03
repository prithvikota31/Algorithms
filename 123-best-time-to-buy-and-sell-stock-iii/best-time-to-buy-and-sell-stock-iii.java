class Solution {
    public int maxProfit(int[] prices) {
        //f(ind, canBuy, count)
        int n = prices.length;
        int maxTxns = 2;
        int buyPossibilities = 2;
        int[][][] dp = new int[n][buyPossibilities][maxTxns];

        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < maxTxns; j++)
            {
                for(int k = 0; k < buyPossibilities; k++)
                {
                    dp[i][j][k] = -1;
                }
            }
        }

        int ans = calculateProfit(0, 1, 2, prices, dp);
        return ans;
    }

    private int calculateProfit(int index, int canBuy, int txnCount, int[] prices, int[][][] dp)
    {
        if(txnCount == 0 || index == prices.length)
        {
            //index == n means all prices/days are completed, no way to realize anything further
            return 0;
        }

        if(dp[index][canBuy][txnCount - 1] != -1)
        {
            return dp[index][canBuy][txnCount - 1];
        }
        int profit = 0;
        if(canBuy == 1)
        {
            profit = Math.max(-prices[index] + calculateProfit(index + 1, 0, txnCount, prices, dp), 
                        0 + calculateProfit(index + 1, 1, txnCount, prices, dp));
        }
        else //can sell
        {
            profit = Math.max(prices[index] + calculateProfit(index + 1, 1, txnCount - 1, prices, dp), 
                0 + calculateProfit(index + 1, 0, txnCount, prices, dp));
        }

        return dp[index][canBuy][txnCount - 1] = profit;
    }


}