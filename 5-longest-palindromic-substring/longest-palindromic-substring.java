class Solution {
    private int start = 0;
    private int end = 0;
    public String longestPalindrome(String s) {
        int n = s.length();
        int max = 0;
        for(int i = 0; i <= n - 1; i++)
        {
            checkPalindrome(s, i, i);
            // if(i != n - 1 && s.charAt(i) == s.charAt(i + 1))
            // {
            //     checkPalindrome(s, i, i + 1);
            // }
            checkPalindrome(s, i, i + 1);
        }


        return s.substring(start, end + 1);
  
    }


    private void checkPalindrome(String s, int i, int j)
    {
        int leftBound = 0;
        int rightBound = s.length() - 1;
        while(i >= leftBound && j <= rightBound)
        {
            if(s.charAt(i) == s.charAt(j))
            {
                if(end - start + 1 < j - i + 1)
                {
                    start = i;
                    end = j;
                }
                i--;
                j++;
            }
            else
            {
                break;
            }
        }
    }
}