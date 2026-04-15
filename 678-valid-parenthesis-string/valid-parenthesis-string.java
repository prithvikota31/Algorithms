class Solution {
    public boolean checkValidString(String s) {
        int l = s.length();

        if(s.charAt(0) == ')' || s.charAt(l-1) == '(')
        {
            return false;
        }

        int min = 0;
        int max = 0;

        for(char c : s.toCharArray())
        {
            if(c == '(')
            {
                max++;
                min++;
            }
            if(c == ')')
            {
                max--;
                min--;
            }
            if(c == '*')
            {
                max++;
                min--;
            }

            if(max < 0)
            {
                return false;
            }

            if(min < 0)
            {
                min = 0;
            }

        }

        if(min != 0)
        {
            return false;
        }

        return true;


        
    }
}