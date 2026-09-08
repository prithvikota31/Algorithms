class Solution {
    public List<List<String>> partition(String s) {
        List<List<String>> ans = new ArrayList<>();
        getAllPartitions(s, ans, 0, new ArrayList<>());
        return ans;
    }

    private void getAllPartitions(String s, List<List<String>> ans, int ind, List<String> trackingList)
    {
        if(ind == s.length())
        {
            ans.add(new ArrayList<>(trackingList));
            return;
        }
        for(int i = ind; i < s.length(); i++)
        {
            if(isPalindrome(s, ind, i))
            {
                trackingList.add(s.substring(ind, i + 1));
                getAllPartitions(s, ans, i + 1, trackingList);
                trackingList.remove(trackingList.size() - 1);
            }
        }
    }

    private boolean isPalindrome(String s, int i , int j)
    {
        while(i <= j)
        {
            if(s.charAt(i) == s.charAt(j))
            {
                i++;
                j--;
            }
            else
            {
                return false;
            }
        }

        return true;
    }


}