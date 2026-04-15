class Solution {
    public boolean checkValidString(String s) {
        //capture open brackets
        int minOpen = 0;
        int maxOpen = 0;

        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '(')
            {
                minOpen++;
                maxOpen++;
            }
            else if(s.charAt(i) == ')')
            {
                minOpen--;
                maxOpen--;
                if(maxOpen < 0) return false;
            }
            else // *
            {
                minOpen--;
                maxOpen++;
            }

           
            minOpen = Math.max(minOpen, 0);
        }
        
        if(minOpen != 0)    return false;
        else    return true;
    }
}