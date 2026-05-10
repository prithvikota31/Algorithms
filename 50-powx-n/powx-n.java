class Solution {
    public double myPow(double x, int n) {
        int power = n;
        if(n < 0)
        {
            x = 1/x;
            power = -n;
        }

        return solve(x, power);
        
    }

    public double solve(double x, int n)
    {
        double ans = 1;
        if(n == 0)
        {
            return 1;
        }
        double half = solve(x, n/2);

        if(n%2 == 0)
        {
            ans = half * half;
        }
        else
        {
            ans = x * half * half;
        }

        return ans;

    }
}