class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        if(m > n)   return false;

        int[] freq = new int[26];
        int checksNeeded = 0;
        for(int i = 0; i < m; i++)
        {
            freq[s1.charAt(i) - 'a']++;
            checksNeeded++;
        }

        int start = 0;
        for(int end = 0; end < s2.length(); end++)
        {
            int ch = s2.charAt(end) - 'a';
            freq[ch]--;
            if(freq[ch] >= 0)
            {
                checksNeeded--;
            }
            if(end - start + 1 > m)
            {
                //move start
                freq[s2.charAt(start) - 'a']++;
                if(freq[s2.charAt(start) - 'a'] > 0)
                {
                    checksNeeded++;
                }
                start++;
            }

            if(checksNeeded == 0)
            {
                return true;
            }
        }
        return false;
    }
}