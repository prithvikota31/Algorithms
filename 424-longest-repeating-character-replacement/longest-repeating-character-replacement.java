class Solution {
    public int characterReplacement(String s, int k) {
        // Frequency of characters in current window
        int[] freq = new int[26];

        int l = 0, r = 0;
        int maxFreq = 0;   // highest frequency of a single character in the window
        int maxLen = 0;    // best valid window length seen so far

        while (r < s.length()) {
            // Expand window to the right and update frequency
            int idx = s.charAt(r) - 'A';
            freq[idx]++;
            maxFreq = Math.max(maxFreq, freq[idx]);

            int windowLen = r - l + 1;

            // If we can replace at most k characters to make the window uniform
            if (windowLen - maxFreq <= k) {
                // Valid window → try to maximize length
                maxLen = Math.max(maxLen, windowLen);
            // Note: We do NOT recompute or decrease maxFreq when the left pointer moves.
            // maxFreq may become stale, but that’s OK:
            // - A stale (larger) maxFreq only makes (windowLen - maxFreq) smaller,
            //   which may allow a slightly invalid window to be treated as valid.
            // - This does NOT affect correctness because we are only interested in
            //   the maximum window length, and window size only increases when valid.
            // - Recomputing maxFreq would cost O(26) per step and is unnecessary.

            } else {
                // Too many replacements needed → shrink from the left
                freq[s.charAt(l) - 'A']--;
                l++;
            }

            r++;
        }

        return maxLen;
    }
}
