class Solution {
    public int[] plusOne(int[] digits) {
        int n = digits.length;

        // Start from the last digit because +1 affects the rightmost side first.
        for (int i = n - 1; i >= 0; i--) {

            // If current digit is not 9, we can safely add 1 and finish.
            // No more carry needed.
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }

            // If current digit is 9, 9 + 1 becomes 0 with carry to the left.
            digits[i] = 0;
        }

        // If we reached here, all digits were 9.
        // Example: 999 + 1 = 1000
        int[] result = new int[n + 1];
        result[0] = 1;

        return result;
    }
}