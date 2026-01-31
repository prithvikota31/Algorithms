
class Solution {
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        int[] freq = new int[26];

        // Step 1: build frequency map for s1
        for (char c : s1.toCharArray()) {
            freq[c - 'a']++;
        }

        int left = 0;
        int right = 0;
        int needed = s1.length();

        // Step 2: sliding window over s2
        while (right < s2.length()) {
            char rc = s2.charAt(right);

            if (freq[rc - 'a'] > 0) {
                needed--;
            }
            freq[rc - 'a']--;
            

            // Step 3: if window size > s1, shrink from left
            if (right - left >= s1.length()) {
                char lc = s2.charAt(left);
                if (freq[lc - 'a'] >= 0) {
                    needed++;
                }
                freq[lc - 'a']++;
                left++;
            }

            // Step 4: check match
            if (needed == 0) return true;
            right++;
        }

        return false;
    }
}
