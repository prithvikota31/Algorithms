class Solution {
    public String minWindow(String s, String t) {
        //every character in t should be in s

        int sLen = s.length();
        int tLen = t.length();

        //sLen> = tLen
        if(sLen < tLen) return "";

        int start = 0;
        int minLen = Integer.MAX_VALUE;
        int bestStart = -1;

        int[] tFreq = new int[128];
        for(char c: t.toCharArray())
        {
            tFreq[c]++;
        }

        int checksNeeded = tLen;
        for(int end = 0; end < sLen; end++)
        {
            char endCh = s.charAt(end);

            if(tFreq[endCh] > 0)
            {
                checksNeeded--;
            }
            tFreq[endCh]--;

            while(start <= end && checksNeeded == 0)
            {
                if(end - start + 1 < minLen)
                {
                    minLen = end - start + 1;
                    bestStart = start;
                }
                //try moving the start
                char startCh = s.charAt(start);
                if(tFreq[startCh] >= 0)
                {
                    checksNeeded++;
                }
                tFreq[startCh]++;
                start++;
            }
        }

        return minLen == Integer.MAX_VALUE? "": s.substring(bestStart, bestStart + minLen);
    }
}