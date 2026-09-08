class Solution {
    public List<Integer> partitionLabels(String s) {
        int l = s.length();

        Map<Character, Integer> endingIndexMap = new HashMap<>();

        for(int i = 0; i < s.length(); i++)
        {
            endingIndexMap.put(s.charAt(i), i);
        }
        List<Integer> ans = new ArrayList<>();
        int start = 0;
        int end = 0;

        for(int i = 0; i < l; i++)
        {
            char ch = s.charAt(i);
            end = Math.max(end, endingIndexMap.get(ch));

            if(i == end)
            {
                ans.add(end - start + 1);
                start = end + 1;
            }
        }

        return ans;
    }
}