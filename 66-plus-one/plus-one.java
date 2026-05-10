class Solution {
    public int[] plusOne(int[] digits) {
        
        int left = 0;
        int right = digits.length - 1;

        int index = right;
        int carry = 1;
        while(index >= left || carry == 0)
        {
            if(digits[index] == 9)
            {
                digits[index] = 0;
                carry = 1;
            }
            else
            {
                digits[index]++;
                carry = 0;
                break;
            }
            index--;
        }

        if(carry == 0)
        {
            return digits;
        }
        else
        {
            int[] result = new int[digits.length + 1];

            result[0] = 1;
            for(int i = 1; i < result.length; i++)
            {
                result[i] = digits[i - 1];
            }
            return result;
        }
    }
}