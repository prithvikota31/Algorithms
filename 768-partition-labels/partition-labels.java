class Solution {
    public List<Integer> partitionLabels(String s) {
        List<Integer> res = new ArrayList<>();
        int[] lastIndex = new int[26];
        for(int i = 0; i < s.length(); i++)
        {
            lastIndex[s.charAt(i) - 'a'] = i;
        }
        int end = 0;
        int start = 0;
        for(int i = 0; i < s.length(); i++)
        {
            end = Math.max(lastIndex[s.charAt(i) - 'a'], end);

            if(i == end)
            {
                res.add(end - start + 1);
                start = end + 1;
            }
        }

        return res;



    }
}