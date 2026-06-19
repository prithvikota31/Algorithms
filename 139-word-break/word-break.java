class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        int n = s.length();
        Set<String> set = new HashSet<>(wordDict);

        boolean[] dp = new boolean[n + 1]; //dp[i] represents wordBreak t/f till (i-1)th character

        dp[0] = true;
        for(int i = 1; i <= n; i++)
        {
            for(int j = 0; j < i; j++)
            {
                if(dp[j] && set.contains(s.substring(j, i)))
                {
                    dp[i] = true;
                    break;
                }
            }
        }

        return dp[n];
    }
}