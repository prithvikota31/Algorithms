class Solution {
    public String minWindow(String s, String t) {
        int[] hash = new int[256];

        for(char c: t.toCharArray())
        {
            hash[c]++;
        }

        int r = 0, l = 0, minLen = Integer.MAX_VALUE, startIndex = -1;
        int count = 0;
        int required = t.length();
        while(r < s.length())
        {
            char rc = s.charAt(r);
            if(hash[rc] > 0)
            {
                count++;
            }
            hash[s.charAt(r)]--;

            while(count == required){
                if(r - l + 1 < minLen)
                {
                    minLen = r - l + 1;
                    startIndex = l;
                }
                char lc = s.charAt(l);
                if(hash[lc] >= 0)   count--;
                hash[lc]++;
                l++;

            }

            r++;
        }

        return startIndex == -1? "" : s.substring(startIndex, startIndex + minLen);
    }
}