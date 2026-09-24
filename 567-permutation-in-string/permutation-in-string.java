class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int l1 = s1.length();
        int l2 = s2.length();
        //s2 >= s1
        //s1 
        if(l1 > l2) return false;

        int[] freq = new int[26];

        for(int i = 0; i < s1.length(); i++)
        {
            freq[s1.charAt(i) - 'a']++;
        }

        int checksNeeded = s1.length();

        int left = 0;

        for(int right = 0; right < s2.length(); right++)
        {
            //expand right
            //if len > s1.length()
            //shrink it

            char rightCh = s2.charAt(right);
            if(freq[rightCh - 'a'] > 0)
            {
                checksNeeded--;           
            }
            freq[rightCh - 'a']--;

            if(right - left + 1 > s1.length())
            {
                //shrink left
                char leftCh = s2.charAt(left);
                if(freq[leftCh - 'a'] >= 0)
                {
                    checksNeeded++;
                }
                freq[leftCh - 'a']++;
                left++;
            }

            if(checksNeeded == 0)
            {
                return true;
            }
        }

        return false;
    }
}