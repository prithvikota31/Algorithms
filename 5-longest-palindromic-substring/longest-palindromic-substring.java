class Solution {

    private int startFinal = -1;
    private int endFinal = -1;
    private int maxLength = -1;
    public String longestPalindrome(String s) {
        for(int i = 0; i < s.length(); i++)
        {
            expand(s, i, i);//odd length
            expand(s, i, i + 1);//even length
        }

        return s.substring(startFinal, endFinal);
    }

    private void expand(String s, int start, int end)
    {
        while(start >= 0 && end <= s.length() - 1 && s.charAt(start) == s.charAt(end))
        {
            start--;
            end++;
        }
        //before breaking the loop, start and end just came out of the palindrome
        //atual range start + 1, end - 1
        int length = end - start - 1;
        if(length > maxLength)
        {
            maxLength = length;
            startFinal = start + 1;
            endFinal = end;
        }

    }
}