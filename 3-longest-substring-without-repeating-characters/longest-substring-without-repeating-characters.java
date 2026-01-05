class Solution {
    public int lengthOfLongestSubstring(String s) {
        // a b c c a b c b b
        //brute force
        //explore all possibilites
        //i ->  0 - (n - 1
        // j -> i - (n -1)
            // int maxLen = 0;
            // for(int i = 0; i < s.length(); i++)
            // {
            //     int[] hash = new int[256];
            //     for(int j = i; j < s.length(); j++)
            //     {
            //         if(hash[s.charAt(j)] == 1)  break;
            //         hash[s.charAt(j)] = 1; // marked found
            //         maxLen = Math.max(maxLen, j - i + 1);

            //     }
            // }

        //2 pointers optimal
        //a b c a b c b b
        //l
        //r


        int l = 0, r = 0, maxLen = 0;
        int[] hash = new int[256];
        Arrays.fill(hash, -1);
        while(r < s.length())
        {
            if(hash[s.charAt(r)] != -1) //if index is already presen
            {
                if(hash[s.charAt(r)] >= l)
                    l = hash[s.charAt(r)] + 1;
            }
            hash[s.charAt(r)] = r;
            maxLen = Math.max(maxLen, r - l + 1);
            r++;

        }


        return maxLen;
        
    }
}