class Solution {
    public double myPow(double x, int n) {
        long N = n;
        if(N < 0)
        {
            N = -N;
            x = 1.0 / x;
        }

        return powPositiveN(x, N);
    }

    public double powPositiveN(double x, long n)
    {
        if(n == 1)  return x;
        if(n == 0)  return 1;
        if(n % 2 == 0)
        {
            return powPositiveN(x * x, n / 2);
        }
        else
        {
            return x * powPositiveN(x * x, (n - 1)/ 2);
        }
    }
}