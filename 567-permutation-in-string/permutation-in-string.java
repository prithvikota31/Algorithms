class Solution {
    public boolean checkInclusion(String s1, String s2) {

        // Impossible case
        if (s1.length() > s2.length()) {
            return false;
        }

        int[] s1Freq = new int[26];
        int[] windowFreq = new int[26];

        // Build frequency for s1
        for (char c : s1.toCharArray()) {
            s1Freq[c - 'a']++;
        }

        int windowSize = s1.length();

        // Build first window
        for (int i = 0; i < windowSize; i++) {
            windowFreq[s2.charAt(i) - 'a']++;
        }

        // Check first window
        if (Arrays.equals(s1Freq, windowFreq)) {
            return true;
        }

        // Slide the window
        for (int right = windowSize; right < s2.length(); right++) {

            // Add new character
            windowFreq[s2.charAt(right) - 'a']++;

            // Remove left character
            windowFreq[s2.charAt(right - windowSize) - 'a']--;

            // Compare frequencies
            if (Arrays.equals(s1Freq, windowFreq)) {
                return true;
            }
        }

        return false;
    }
}