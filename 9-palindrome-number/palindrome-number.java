class Solution {
    public boolean isPalindrome(int x) {
        if(x < 0)   return false;
        long result = 0;
        long current = (long)x;

        while(current != 0)
        {
            result = result * 10 + (current % 10);
            current = current / 10;
        }

        if(result == (long)x)
        {
            return true;
        }
        else
            return false;
    }
}