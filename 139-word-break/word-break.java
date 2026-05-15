class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        int n = s.length();
        boolean[] dp = new boolean[n];
        Set<String> set = new HashSet<>(wordDict);

        dp[0] = set.contains(s.substring(0, 1));
        System.out.println(set.size());

        for(int i = 1; i < n; i++)
        {
            for(int j = 0; j <= i; j++)
            {
                //dp[j] represents validdty of space sepration till j
                if(j == 0)
                {
                    dp[i] = set.contains(s.substring(j, i + 1));
                }
                else
                {
                    dp[i] = dp[j - 1] && set.contains(s.substring(j, i + 1));
                } 
                if(dp[i]){
                    break;
                }
            }
        }

        return dp[n - 1];
    }
}