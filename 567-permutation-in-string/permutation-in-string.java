class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        if(m > n)   return false;

        int[] freq = new int[26];
        int checksNeeded = 0;
        for(char ch: s1.toCharArray())
        {
            freq[ch - 'a']++;
            checksNeeded++;
        }

        int start = 0;
        for(int end = 0; end < n; end++)
        {
            if(freq[s2.charAt(end) - 'a'] > 0)
            {
                checksNeeded--;
            }
            freq[s2.charAt(end) - 'a']--;

            if(end - start + 1 > m) //substring length - make it m
            {
                if(freq[s2.charAt(start) - 'a'] >= 0)
                {
                    checksNeeded++;
                }
                freq[s2.charAt(start) - 'a']++;
                start++;
            }

            if(checksNeeded == 0)   return true;

        }

        return false;
    }
}