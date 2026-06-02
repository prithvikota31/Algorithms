class Solution {
    public String minWindow(String s, String t) {
        int m = s.length();
        int n = t.length();

        if(n > m)   return "";

        int[] freq = new int[128];

        for(char c: t.toCharArray())
        {
            freq[c]++; 
        }

        int start = 0;
        int minLength = Integer.MAX_VALUE;
        int checksNeeded = n;
        int finalStartIndex = 0;
        for(int end = 0; end < m; end++)
        {
            char ch = s.charAt(end);
            if(freq[ch] > 0)
            {
                checksNeeded--;
            }
            freq[ch]--;

            while(checksNeeded == 0)
            {
                if(end - start + 1 < minLength)
                {
                    minLength = end - start + 1;
                    finalStartIndex = start;
                }
                int startChar = s.charAt(start);
                if(freq[startChar] >= 0)
                {
                    checksNeeded++;
                }
                freq[startChar]++;
                start++;
            }
        }

        if(minLength == Integer.MAX_VALUE)
        {
            return "";
        }

        return s.substring(finalStartIndex, finalStartIndex + minLength);

        
    }
}