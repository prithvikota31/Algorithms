class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        //char 'x' -> lastKnownIndex of char 'x'

        int start = 0;
        int maxLength = 0;

        for(int end = 0; end < s.length(); end++)
        {
            char ch = s.charAt(end);

            if(map.containsKey(ch))
            {
                start = Math.max(start, map.get(ch) + 1);
            }

            map.put(ch, end);

            if(end - start + 1 > maxLength)
            {
                maxLength = end - start + 1;
            }
        }

        return maxLength;
    }
}