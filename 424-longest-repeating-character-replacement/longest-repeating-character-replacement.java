class Solution {
    public int characterReplacement(String s, int k) {
        // AABABBA

        int[] freq = new int[26];
        int l = 0, r = 0, maxFreq = 0, maxLen = 0;

        while(r < s.length())
        {
            freq[s.charAt(r) - 'A']++;
            maxFreq = Math.max(maxFreq, freq[s.charAt(r) - 'A']);

            int length = r - l + 1;


            if(length - maxFreq <= k)
            {
                maxLen = Math.max(maxLen, length);
            }

            if(length - maxFreq > k)
            {
                freq[s.charAt(l) - 'A']--;
                l++;
            }
            r++;
        }

        return maxLen;
    }
}