class Solution {
    public boolean checkInclusion(String s1, String s2) {
        if(s1.length() > s2.length())   return false;

        int required = s1.length();
        int[] freq = new int[26];
        //fill frequency for string s1
        for(char c: s1.toCharArray())
        {
            freq[c - 'a']++;
        }

        //traverse s2 till first window
        int acquired = 0;
        for(int i = 0; i < s1.length(); i++)
        {
            char s2c = s2.charAt(i);
            if(freq[s2c - 'a'] > 0) {
                acquired++;
            }
            freq[s2c - 'a']--; // all freq which are not present in s1 goes -ve(useful later)
        }

        

        // assume s1 = 2 and s2 = 5
        //assume i starts from 1 it goes till 4(s2 - s1 + 1)
        // 0 1 2 3 4 
        // i = 0 to 3
        //fixed window moved from i = 1 to (s2 - s1 + 1)
        //a = 0
        //b = 2
        //c = -2

        for(int i = 0; i < s2.length() - s1.length(); i++)
        {
            if(acquired == required)    return true; //entering the loop itself we have the perfect acquired count
            //now to move the window check if acquired has to be touched or not
            char startRemove = s2.charAt(i);
            char endAdd = s2.charAt(i + s1.length());
            if(freq[startRemove - 'a'] >=  0)   
            {
                acquired--;
                //increment that freq?
            }
            freq[startRemove - 'a']++;
            if(freq[endAdd - 'a'] > 0)
            {
                acquired++;    
            }
            freq[endAdd - 'a']--;

        }
         if(acquired == required)    return true;

        return false;


    }
}