class Solution {
    public List<List<String>> partition(String s) {
        int l = s.length();
        List<List<String>> ans = new ArrayList<>();

        List<String> curr = new ArrayList<>();

        backtracking(s, ans, curr, 0);

        return ans;
    }
    
    public void backtracking(String s, List<List<String>> ans, List<String> curr , int index)
    {
        if(index == s.length())
        {
            ans.add(new ArrayList<>(curr));
            return;
        }

        for(int i = index; i < s.length(); i++)
        {
            if(isPalindrome(s, index, i))
            {
                curr.add(s.substring(index, i+1));
                backtracking(s, ans, curr, i+1);
                curr.remove(curr.size() - 1);
            }
        }
    }

    public boolean isPalindrome(String s, int l, int r)
    {
        while(l <= r)
        {
            if(s.charAt(l) != s.charAt(r))
            {
                return false;
            }

            l++;
            r--;
        }

        return true;
    }
}