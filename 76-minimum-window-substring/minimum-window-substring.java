class Solution {
    public String minWindow(String s, String t) {
        //expand window outard and then shrink

        int m = s.length();
        int n = t.length();

        if(n > m)   return "";

        
        int[] freq = new int[128];
        for(char c: t.toCharArray())
        {
            freq[c]++;
        }
        int checksNeeded = t.length();

        int start = 0;
        int minLength = Integer.MAX_VALUE;
        int startIndex = 0;
        for(int end = 0; end < s.length(); end++)
        {
            if(freq[s.charAt(end)] > 0) //we still need them
            {
                checksNeeded--;
            }
            freq[s.charAt(end)]--;

            while(checksNeeded == 0)
            {
                if(end - start + 1 < minLength)
                {
                    minLength = end - start + 1;
                    startIndex = start;
                }

                //try to reduce from forward
                if(freq[s.charAt(start)] >= 0)
                {
                    checksNeeded++;
                }

                freq[s.charAt(start)]++;
                start++;
            }
        }

        if(minLength == Integer.MAX_VALUE)  return "";
        else return s.substring(startIndex, startIndex + minLength);
    }
}