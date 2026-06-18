class Solution {
    public List<List<String>> partition(String s) {
        List<List<String>>  result = new ArrayList<>();

        backtrack(result, 0, s, new ArrayList<>());
        return result;
    }

    private void backtrack(List<List<String>>  result, int ind, String s, List<String> tracking)
    {
        if(ind == s.length())
        {
            result.add(new ArrayList<>(tracking));
            return;
        }

        for(int i = ind; i < s.length(); i++)
        {
            if(isPalindrome(s, ind, i))
            {
                tracking.add(s.substring(ind, i + 1));
                backtrack(result, i + 1, s, tracking);
                tracking.remove(tracking.size() - 1);
            }
        }
    }


    private boolean isPalindrome(String s, int i, int j)
    {
        while(i <= j)
        {
            if(s.charAt(i) != s.charAt(j))
            {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }
}