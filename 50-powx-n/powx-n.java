class Solution {
    public double myPow(double x, int n) {
        long N = n;
        if(N < 0)
        {
            N = -N;
            x = (1.0 / x);
        }

        return powCal(x, N);
    }

    private double powCal(double x, long n) // x > 0 and n +ve
    {   
        if(n == 0){
            return 1;
        }
        if(n == 1)
        {
            return x;
        }

        if(n % 2 == 0)
        {
            return powCal(x * x, n / 2);
        }
        else
        {
            return x * powCal(x * x, (n - 1) / 2);
        }

    }
}