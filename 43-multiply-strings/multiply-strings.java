class Solution {

    public String multiply(String num1, String num2) {
        // 99 - j (num2)
        // 98 - i (num1)
        // -----
        //    7 9 2
        // 9  0 1
        // edge case
        if (num1.equals("0") || num2.equals("0"))
            return "0";

        int m = num1.length();
        int n = num2.length();

        int[] result = new int[m + n];

        // your loop structure (fixed)
        for (int i = m - 1; i >= 0; i--) {

            int carry = 0;
            int digit1 = num1.charAt(i) - '0';

            for (int j = n - 1; j >= 0; j--) {

                int digit2 = num2.charAt(j) - '0';

                int sum = digit1 * digit2 + result[i + j + 1] + carry;

                result[i + j + 1] = sum % 10;
                carry = sum / 10;
            }

            // store leftover carry
            result[i] += carry;
        }

        // convert result array to string
        StringBuilder sb = new StringBuilder();

        // for (int num : result) {

        //     // skip leading zeros
        //     if (sb.length() != 0 || num != 0)
        //         sb.append(num);
        // }

        int start = 0;
        while(start < m + n)
        {
            if(result[start] == 0)
            {
                start++;
                continue;
            }
            else
            {
                break;
            }
        }

        for(int i = start; i < m + n; i++)
        {
            sb.append(result[i]);
        }

        return sb.toString();
    }
}