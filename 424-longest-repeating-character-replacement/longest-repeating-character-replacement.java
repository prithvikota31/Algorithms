class Solution {
    public int characterReplacement(String s, int k) {
        int n = s.length();
        int[] freq = new int[26];

        int l = 0;
        int r = 0;
        int maxFreq = 0;
        int maxLength = 0;
        while(r < n)
        {
            freq[s.charAt(r) - 'A']++;
            maxFreq = Math.max(maxFreq, freq[s.charAt(r) - 'A']);
            int len = r - l + 1;
            if(len - maxFreq <= k)
            {
                maxLength = Math.max(maxLength, len);
            }
            else
            {
                freq[s.charAt(l) - 'A']--;
                l++;
            }
            r++;
        }

        return maxLength;

    }
}