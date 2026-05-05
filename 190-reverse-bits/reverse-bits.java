class Solution {
    public int reverseBits(int n) {
        int ans = 0;

        for(int i = 0; i < 32; i++)
        {
            int lastbit = n & 1;
            n = n >>> 1;
            ans = ans << 1;  
            ans = ans | lastbit;
            
        }

        return ans;
    }
}