class Solution {
    public int characterReplacement(String s, int k) {
        int[] frequency = new int[26];
        int maxFreq = 0;
        int l = 0, r= 0;
        int maxLen = 0;
        while(r < s.length())
        {
            frequency[s.charAt(r) - 'A']++;
            maxFreq = Math.max(maxFreq, frequency[s.charAt(r) - 'A']);

            if((r - l + 1) - maxFreq > k)
            {
                frequency[s.charAt(l) - 'A']--;
                l++;
            }

            maxLen = Math.max(maxLen, r - l + 1);
            r++;
        }

        return maxLen;
    }
}