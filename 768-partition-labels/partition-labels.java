class Solution {
    public List<Integer> partitionLabels(String s) {
        List<Integer> result = new ArrayList<>();

        Map<Character, Integer> map = new HashMap<>();
        for(int i = 0; i < s.length(); i++)
        {
            map.put(s.charAt(i), i);
        }

        int start = 0;
        int end = 0;

        for(int i = 0; i < s.length(); i++)
        {
            end = Math.max(end, map.get(s.charAt(i)));

            if(i == end)
            {
                result.add(end - start + 1);
                start = end + 1;
            }
        }

        return result;
    }
}