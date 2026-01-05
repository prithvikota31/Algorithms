class Solution {
    public int lengthOfLongestSubstring(String s) {
        // a b c c a b c b b
        //brute force
        //explore all possibilites
        //i ->  0 - (n - 1
        // j -> i - (n -1)
        int maxLen = 0;
        for(int i = 0; i < s.length(); i++)
        {
            int[] hash = new int[256];
            for(int j = i; j < s.length(); j++)
            {
                if(hash[s.charAt(j)] == 1)  break;
                hash[s.charAt(j)] = 1; // marked found
                maxLen = Math.max(maxLen, j - i + 1);

            }
        }

        return maxLen;
        
    }
}