class Solution {
    public int characterReplacement(String s, int k) {
        int[] freq = new int[26];

        int start = 0;
        int maxFreq = 0;
        int maxLen = 0;

        for(int end = 0; end < s.length(); end++)
        {
            int chEnd = s.charAt(end) - 'A';
            freq[chEnd]++;
            maxFreq = Math.max(maxFreq, freq[chEnd]);

            if((end - start + 1) - maxFreq > k)
            {
                int chStart = s.charAt(start) - 'A';
                freq[chStart]--;
                start++;
            }
            maxLen = Math.max(maxLen, end - start + 1);
        }

        return maxLen;
    }
}