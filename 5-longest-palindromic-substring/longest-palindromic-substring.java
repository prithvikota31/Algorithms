class Solution {

    private int maxLength = 1;
    private int startF = 0;
    private int endF = 1;
    public String longestPalindrome(String s) {
        int l = s.length();

        for(int i = 0; i < l; i++)
        {
            expand(s, i, i); //odd length;
            expand(s, i, i + 1); // even length;
        }

        return s.substring(startF, endF);
    }

    private void expand(String s, int start, int end)
    {
        while(start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end))
        {
            start--;
            end++;
        }

        int palindromeLength = end - start - 1;
        if(palindromeLength > maxLength)
        {
            startF = start + 1;
            endF = end;
            maxLength = palindromeLength;
        }

    }
}