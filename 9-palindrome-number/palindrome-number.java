class Solution {
    public boolean isPalindrome(int x) {
        // Negative numbers cannot be palindromes because of the '-' sign.
        if (x < 0) return false;

        long reversed = 0;
        long current = x;

        // Reverse the digits of x.
        while (current != 0) {
            reversed = reversed * 10 + current % 10;
            current /= 10;
        }

        // Palindrome if reversed number equals original number.
        return reversed == x;
    }
}