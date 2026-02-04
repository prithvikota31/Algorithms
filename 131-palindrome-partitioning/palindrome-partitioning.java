class Solution {
    public List<List<String>> partition(String s) {
      List<List<String>> result = new ArrayList<>();
      List<String> trackingList = new ArrayList<>();

      
      findPartitions(result, trackingList, s, 0);
      return result;
    }


    public void findPartitions(List<List<String>> result, List<String> trackingList, String s, int index)
    {
        if(index == s.length())
        {
            result.add(new ArrayList<>(trackingList));
        }
        for(int i = index; i < s.length(); i++)
        {
            if(isPalindrome(s, index, i))
            {
                trackingList.add(s.substring(index, i + 1));
                findPartitions(result, trackingList, s, i + 1);
                trackingList.remove(trackingList.size() - 1);
            }
        }
    }   

    public boolean isPalindrome(String s, int start, int end)
    {
        while(start <= end)
        {
            if(s.charAt(start++) != s.charAt(end--))
            {   
                return false;
            }
        }
        return true;
    }
}

