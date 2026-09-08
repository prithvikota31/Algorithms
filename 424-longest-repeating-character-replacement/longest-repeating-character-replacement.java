class Solution {
    public int characterReplacement(String s, int k) {
        int n = s.length();
        int start = 0;
        int maxLength = 0;

        int[] freq = new int[26];
        int maxFreq = 0;
        for(int end = 0; end < n; end++)
        {
            char ch = s.charAt(end);
            freq[ch - 'A']++;
            maxFreq = Math.max(maxFreq, freq[ch - 'A']);

            int length = end - start + 1;
            if(length - maxFreq <= k)
            {
                maxLength = Math.max(maxLength, length);
            }
            else
            {
                freq[s.charAt(start) - 'A']--;
                start++;
                
            }
        }
        return maxLength;
    }
}