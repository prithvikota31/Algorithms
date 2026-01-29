class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        //key uniqe pattern of anagram, corresponding list 
        Map<String, List<String>> map = new HashMap<>();

        for(String s: strs)
        {
            int[] frequency = new int[26];
            for(char c: s.toCharArray())
            {
                frequency[c - 'a']++;
            }

            String key = Arrays.toString(frequency);

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }
}