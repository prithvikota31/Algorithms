class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        int l = s.length();
        boolean[] dp = new boolean[l+1];

        Set<String> set = new HashSet<>(wordDict);

        dp[0] = true;

        for(int i = 1; i <= l; i++)
        {
            for(int j = 0; j <= i; j++)
            {
                if(dp[j] && set.contains(s.substring(j, i)))
                {
                    dp[i] = true;
                    break;
                }
            }
        }

        return dp[l];


        
    }
}