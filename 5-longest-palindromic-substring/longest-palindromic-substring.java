class Solution {
    int maxLen = 1;
    int bestStart = 0;
    int bestEnd = 0;
    public String longestPalindrome(String s) {
     //at every index, expand odd length and even length


        for(int i = 0; i < s.length(); i++)
        {
            expand(s, i, i);
            expand(s, i, i + 1);
        }

        return s.substring(bestStart, bestEnd + 1);
    }

    private void expand(String s, int left, int right)
    {
        while(left >= 0 && right <= s.length() - 1 && s.charAt(left) == s.charAt(right))
        {
            left--;
            right++;
        }

        int len = right - left - 1;
        if(len > maxLen)
        {
            maxLen = len;
            bestStart = left + 1;
            bestEnd = right - 1;
        }
    }
}