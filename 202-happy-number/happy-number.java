class Solution {
    public boolean isHappy(int n) {
        Set<Integer> set = new HashSet<>();
        while(n != 1)
        {
            if(!set.contains(n))
            {
                set.add(n);
            }
            else
            {
                return false;
            }
            n = sumOfSquares(n);
        }

        return true;

    }

    public int sumOfSquares(int n)
    {
        int sum = 0;
        while(n != 0)
        {
            int digit = n % 10;
            sum += (digit * digit);
            n = n / 10;
        }

        return sum;
    }
}