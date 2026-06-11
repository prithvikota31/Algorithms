class Solution {
    public String minWindow(String s, String t) {
        int[] freq = new int[128];

        int sl = s.length();
        int tl = t.length();

        if(tl > sl) return "";

        int checksNeeded = 0;
        for(char ch: t.toCharArray())
        {
            freq[ch]++;
            checksNeeded++;
        }

        //now sweep through s

        int start = 0;
        int minLength = Integer.MAX_VALUE;
        int startIndex = 0;
        for(int end = 0; end < sl; end++)
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

        if(minLength == Integer.MAX_VALUE)
        {
            return "";
        }

        return s.substring(startIndex, startIndex + minLength);
    }
}