class Solution {
    public boolean isAnagram(String s, String t) {

        // lengths must match
        if(s.length() != t.length()) {
            return false;
        }

        int[] freq = new int[26];

        // count characters
        for(int i = 0; i < s.length(); i++) {

            freq[s.charAt(i) - 'a']++;

            freq[t.charAt(i) - 'a']--;
        }

        // verify all balanced
        for(int count : freq) {
            if(count != 0) {
                return false;
            }
        }

        return true;
    }
}