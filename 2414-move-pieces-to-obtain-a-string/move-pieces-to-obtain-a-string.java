class Solution {
    public boolean canChange(String start, String target) {
        //start and target len are same
        //start(R) > target(R) index return false
        //start(L) < target(L)


        int s = 0;
        int t = 0;

        int n = start.length();

        while(s < n && t < n)
        {
            while(s < n && start.charAt(s) == '_')
            {
                s++;
                continue;
            }

            while(t < n && target.charAt(t) == '_')
            {
                t++;
                continue;
            }

            if(s == n || t == n)
            {
                break;
            }

            if(start.charAt(s) == 'L' && target.charAt(t) == 'L')
            {
                if(s < t)
                {
                    return false;
                }
            }
            else if(start.charAt(s) == 'R' && target.charAt(t) == 'R')
            {
                if(s > t)
                {
                    return false;
                }

            }
            else
            {
                return false;
            }
            s++;
            t++;
        }

        while(s < n && start.charAt(s) == '_')
        {
            s++;
            continue;
        }

        while(t < n && target.charAt(t) == '_')
        {
            t++;
            continue;
        }


        

        if(s == n && t == n)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}