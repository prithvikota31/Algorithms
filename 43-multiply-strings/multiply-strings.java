class Solution {
    public String multiply(String num1, String num2) {
        if(num1.equals("0") || num2.equals("0"))
        {
            return "0";
        }

        int m = num1.length();
        int n = num2.length();

        int[] result = new int[m + n];

        for(int j = n -1; j >= 0; j--)
        {
            int digit2 = num2.charAt(j) - '0';
            for(int i = m - 1; i >= 0; i--)
            {
                int digit1 = num1.charAt(i) - '0';

                int curDigit = i + j + 1;
                int carry = i + j;

                int sum = (digit1 * digit2) + result[curDigit];

                result[curDigit] = sum % 10;
                result[carry] += sum / 10;
            }
        }

        StringBuilder sb = new StringBuilder();

        for(int digit: result)
        {
            if(sb.length() == 0 && digit == 0)
            {
                continue;
            }
            sb.append(digit);
        }

        return sb.toString();
    }
}