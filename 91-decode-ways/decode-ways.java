class Solution {
    public int numDecodings(String s) {
        int n = s.length();

        int[] dp = new int[n + 1];

        dp[0] = 1;

        //dp[i] maps to string index ending at i - 1
        //that is first i characters
        for(int i = 1; i <= n; i++)
        {
            //i as single digit
            if(s.charAt(i - 1) != '0')
            {
                dp[i] += dp[i - 1];
            }

            //ending at i - 1 as double digit
            if(i >= 2)
            {
                int doubleDigit = (s.charAt(i - 2) - '0') * 10 
                                        + (s.charAt(i - 1) - '0');
                if(doubleDigit >= 10 && doubleDigit <= 26)
                {
                    dp[i] += dp[i - 2];
                }

            }

        }

        return dp[n];
    }
}