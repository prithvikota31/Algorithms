
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

                //why >= 0
                //right has already traversed this
                //if the letter doesn't exist at all, it would have become -ve when right traversed
                //which implies, if its freq is zero or >0, since it moves out of the window increased the needed
                //also, this implies when that freq is incrmented it goes to positive which means needed increases

                freq[lc - 'a']++;
                if (freq[lc - 'a'] > 0)  
                { 
                    needed++; //track the needed when moving left and right
                }
                left++;
            }

            // Step 4: check match
            if (needed == 0) return true;
            right++;
        }

        return false;
    }
}
