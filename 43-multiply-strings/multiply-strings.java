class Solution {
    public String multiply(String num1, String num2) {
        int m = num1.length();
        int n = num2.length();
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }

        int[] ans = new int[m + n];


        for(int i = m - 1; i >= 0; i--)
        {
            for(int j = n - 1; j >= 0; j--)
            {
                int digit1 = num1.charAt(i) - '0';
                int digit2 = num2.charAt(j) - '0';

                int currentPos = i + j + 1;
                int carryPos = i + j;

                int product = digit1 * digit2 + ans[currentPos];

                ans[currentPos] = product % 10;
                ans[carryPos] += product / 10;
            }
        }

        StringBuilder sb = new StringBuilder();

        for(int digit: ans)
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