class Solution {
    public int getSum(int a, int b) {
        
        while(b != 0) // b is converted as carry, update a with sum(xor)
        {
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }

        return a;
    }
}