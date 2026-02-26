class Solution {
    public int numDecodings(String s) {
        int n = s.length();
        int[] dp = new int[n + 1];
        dp[n] = 1; //it implies emoty String
        //dp[i] represents total possibilities from i to end
        
        for(int i = n - 1; i >= 0; i--)
        {
            if(s.charAt(i) == '0') //this implies first letter from i is zero
            {
                dp[i] = 0;
                continue;
            }
            dp[i] = dp[i + 1]; //taking i as 1 digit
            if(i + 1 < n)
            {
                //taking i to i + 1 as two digit
                int twoDigit = Integer.parseInt(s.substring(i, i + 2));
                if(twoDigit >= 10 && twoDigit <= 26)
                {
                    dp[i] += dp[i + 2];
                }   
            }
        }

        return dp[0];

    }
}