class Solution {
    public int climbStairs(int n) {
        if(n == 1 || n == 0)  return n;

        int prevOne = 1;
        int prevTwo = 1;
        int current = 0;
        for(int i = 2; i <= n; i++)
        {
            current = prevOne + prevTwo;
            prevOne = prevTwo;
            prevTwo = current;
        }

        return current;
    }
}

