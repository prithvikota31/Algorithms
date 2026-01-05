class Solution {
    public String minWindow(String s, String t) 
    {
        if(s.length() < t.length())   return "";
        Map<Character, Integer> sfreq = new HashMap<>();
        Map<Character, Integer> tfreq = new HashMap<>();

        for(char c : t.toCharArray())
        {
            tfreq.put(c, tfreq.getOrDefault(c, 0) + 1);
        }

        int l = 0, r = 0, minLength = Integer.MAX_VALUE;
        int required = tfreq.size();
        int found = 0;

        // for(int i = 0; i <= r; i++)
        // {
        //     sfreq.put(s.charAt(i), sfreq.getOrDefault(s.charAt(i), 0) + 1);
        // }
        // if(checkIfValid(sfreq, tfreq))
        // {
        //     minLength = Math.min(minLength, r - l + 1);
        //     return s.substring(0, r + 1);
        // }

        // r++;

        // s = "A X B C T U J" t = "A C"
        //l = 0
        //r = 1
        int finalL = 0;
        int finalR = 0;

        while(r < s.length())
        {
            sfreq.put(s.charAt(r), sfreq.getOrDefault(s.charAt(r), 0) + 1);
            if(tfreq.containsKey(s.charAt(r)) && sfreq.get(s.charAt(r)).intValue() == tfreq.get(s.charAt(r)).intValue())
            {
                found++;
            }
            while(found == required)
            {
                if(minLength > r - l + 1)
                {
                    minLength = r - l + 1;
                    finalL = l;
                    finalR = r;
                }
                // minLength = Math.min(minLength, r - l + 1);
                sfreq.put(s.charAt(l), sfreq.get(s.charAt(l)) - 1);
                if(tfreq.containsKey(s.charAt(l)) && sfreq.get(s.charAt(l)) < tfreq.get(s.charAt(l)))
                {
                    found--;
                }
                l++;
            }


            r++;
        }

        return  minLength == Integer.MAX_VALUE? "" : s.substring(finalL, finalR + 1);  
    }

}