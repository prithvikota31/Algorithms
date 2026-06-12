class Solution {
    public int characterReplacement(String s, int k) {
        int[] freq = new int[26];

        int maxSame = 0;
        int start = 0;
        int maxLength = 0;

        for(int end = 0; end < s.length(); end++)
        {

            freq[s.charAt(end) - 'A']++;
            maxSame = Math.max(maxSame, freq[s.charAt(end) - 'A']); //we get better maxLength only if maxSame increases

            int len = end - start + 1;
            if(len - maxSame > k)
            {
                //move start
                freq[s.charAt(start) - 'A']--;
                start++;
                continue;
            }
            maxLength = Math.max(maxLength, len);
        }
        return maxLength;
    }
}