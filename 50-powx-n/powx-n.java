class Solution {
    public double myPow(double x, int n) {
        long p = n;
        double ans = 1;
        if(p<0) {
            p = -1 * p;
        }

        while(p>0) {
            if(p%2==0) {
                x = x * x;
                p = p/2;
            }
            else {
                ans = ans*x;
                p = p-1;
            }
        }

        if(n<0) {
            ans = (double)(1)/(double)(ans);
        }
        return ans;
   
    }
}