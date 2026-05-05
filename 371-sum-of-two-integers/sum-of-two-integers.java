class Solution {
    public int getSum(int a, int b) {

        while(b != 0)
        {
            int sumWithoutCarry = a ^ b;
            b = (a & b) << 1; //carry
            a = sumWithoutCarry;
        }

        return a;
    }
}