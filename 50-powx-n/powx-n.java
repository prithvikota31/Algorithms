class Solution {
    public double myPow(double x, int n) {
        long N = n;

        if(N < 0)
        {
            N = -N;
            x = 1.0/x;
        }

        return myPowPositive(x, N);  
    }

    public double myPowPositive(double x, long N)
    {
        if(N == 1)
        {
            return x;
        }
        if(N == 0)
        {
            return 1;
        }

        if(N%2 == 1)
        {
            return x * myPowPositive(x*x, (N-1)/2);
        }
        else
        {
            return myPowPositive(x*x, N/2);
        }
    }
}