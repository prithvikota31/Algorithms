class Solution {
    public String minWindow(String s, String t) {
        int tLen = t.length();
        int sLen = s.length();

        if(tLen > sLen)
        {
            return "";
        }

        //freq array of t
        int[] freq = new int[128];
        int checksNeeded = 0;
        for(int i = 0; i < tLen; i++)
        {
            freq[t.charAt(i)]++;
            checksNeeded++;
        }
        //freq array contains values > 0 and =0 
        //>0 corresponds to t chars freq

        int start = 0;
        int minLen = Integer.MAX_VALUE;;
        int startIndex = 0;

        for(int end = 0; end < sLen; end++)
        {
            int chEnd = s.charAt(end);
            if(freq[chEnd] > 0)
            {
                checksNeeded--;
            }
            freq[chEnd]--;

            while(checksNeeded == 0)
            {
                //capture this length
                if(end - start + 1 < minLen)
                {
                    minLen = end - start + 1;
                    startIndex = start;
                }
                //we can try to move start 
                int chStart = s.charAt(start);
                freq[chStart]++;
                if(freq[chStart] > 0)
                {
                    checksNeeded++;
                }
                start++;
            }
        }
        if(minLen == Integer.MAX_VALUE)
        {
            return "";
        }

        return s.substring(startIndex, startIndex + minLen);


    }
}