class Solution {
    public int lengthOfLongestSubstring(String s) {
        int start = 0;
        int maxLen = 0;

        int[] lastIndexFound = new int[128];
        Arrays.fill(lastIndexFound, -1);

        for(int end = 0; end < s.length(); end++)
        {
            int chEnd = s.charAt(end);
            if(lastIndexFound[chEnd] != -1 && start <= lastIndexFound[chEnd])
            {
                start = lastIndexFound[chEnd] + 1;
            }

            maxLen = Math.max(maxLen, end - start + 1);
            lastIndexFound[chEnd] = end;
        }
        return maxLen;
    }
}