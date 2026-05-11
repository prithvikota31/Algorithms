class Solution {
    
        

    public int countSubstrings(String s) {
        int n = s.length();
        int count = 0;
        for(int i = 0; i <= n - 1; i++)
        {
            count += checkPalindrome(s, i, i);
            // if(i != n - 1 && s.charAt(i) == s.charAt(i + 1))
            // {
            //     checkPalindrome(s, i, i + 1);
            // }
            count += checkPalindrome(s, i, i + 1);
        }


        return count;
  
    }


    private int checkPalindrome(String s, int i, int j)
    {
        int leftBound = 0;
        int rightBound = s.length() - 1;
        int count = 0;
        while(i >= leftBound && j <= rightBound)
        {
            if(s.charAt(i) == s.charAt(j))
            {
                count++;
                i--;
                j++;
            }
            else
            {
                break;
            }
        }

        return count;
    }
}