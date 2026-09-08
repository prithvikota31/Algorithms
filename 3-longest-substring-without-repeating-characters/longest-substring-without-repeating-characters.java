class Solution {
    public int lengthOfLongestSubstring(String s) {
        int[] last = new int[128];
        Arrays.fill(last, -1);
        int start = 0;
        int maxLength = 0;

        //abcabcbb
        for(int end = 0; end < s.length(); end++)
        {
            char endChar = s.charAt(end);
            if(last[endChar] >= start)
            {
                start = last[endChar] + 1;
            }
            last[endChar] = end;

            maxLength = Math.max(maxLength, end - start + 1);
            
        }
        return maxLength;
    }
}