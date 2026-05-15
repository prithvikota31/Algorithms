class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();

        int start = 0;
        int maxlength = 0;
        for(int end = 0; end < s.length(); end++)
        {
            //cur length = end - start + 1;
            char ch = s.charAt(end); 
            if(map.containsKey(ch) && map.get(ch) >= start)
            {
                start = map.get(ch) + 1;
            }
            map.put(ch, end);
            maxlength = Math.max(maxlength, end - start + 1);
        }

        return maxlength;
    }
}