class Solution {
    public int countSubstrings(String s) {
        int count = 0;
        for(int i = 0; i < s.length(); i++)
        {
            count += expand(s, i, i);
            count += expand(s, i, i + 1);
        }

        return count;
    }

    public int expand(String s, int l, int r)
    {
        int count = 0;
        int n = s.length();
        while(l >= 0 && r < n && s.charAt(l) == s.charAt(r))
        {
            l--;
            r++;
            count++;
        }
        return count;
    }
}