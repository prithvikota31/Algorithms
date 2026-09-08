class Solution {
    public boolean checkValidString(String s) {
        int l = s.length();
        int min = 0;
        int max = 0;
        //range of unmtached ( min and max

        for(int i = 0; i < l; i++)
        {
            char ch = s.charAt(i);
            if(ch == '(')
            {
                min++;
                max++;
            }
            else if(ch == ')')
            {
                min--;
                max--;
            }
            else
            {
                min--;
                max++;
            }

            if(max < 0) return false;
            if(min < 0)
            {
                min = 0;
            }
        }
        return min==0;
    }
}