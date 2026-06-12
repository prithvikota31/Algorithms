class Solution {
    public int characterReplacement(String s, int k) {
        int[] freq = new int[26];

        int start = 0;
        int maxFreq = 0;
        int maxLength = 0;

        for (int end = 0; end < s.length(); end++) {
            char rightChar = s.charAt(end);

            // Add the new character into the current window.
            freq[rightChar - 'A']++;

            // maxFreq = count of the most frequent character in the window.
            // This is the character we pretend we are converting everything else into.
            maxFreq = Math.max(maxFreq, freq[rightChar - 'A']);

            int windowLength = end - start + 1;

            // Replacements needed =
            // total window size - count of majority character.
            //
            // If this exceeds k, the window is invalid, so shrink from the left.
            if (windowLength - maxFreq > k) {
                char leftChar = s.charAt(start);
                freq[leftChar - 'A']--;
                start++;
            }

            // Current window is the best valid candidate after possible shrink.
            maxLength = Math.max(maxLength, end - start + 1);
        }

        return maxLength;
    }
}