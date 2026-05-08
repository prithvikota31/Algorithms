class Solution {
    public List<Integer> partitionLabels(String s) {
        int[] lastIndex = new int[26];
        for(int i = 0; i < s.length(); i++)
        {
            lastIndex[s.charAt(i) - 'a'] = Math.max(lastIndex[s.charAt(i) - 'a'], i);
        }

        List<Integer> result = new ArrayList<>();


        int end = 0;
        int n = s.length();
        int i = 0;
        int start = i;
        while(i < n)
        {

            if(i > end)
            {
                result.add(end - start + 1);
                start = i;
            }
            end = Math.max(end, lastIndex[s.charAt(i) - 'a']);
            i++;
        }

        result.add(end - start + 1);

        return result;
    }
}