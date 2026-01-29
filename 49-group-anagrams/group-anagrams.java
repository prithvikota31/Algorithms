class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        if(strs.length == 0)
        {
            return new ArrayList<>();
        }

        HashMap<String, List<String>> map = new HashMap<>();

        for(String str : strs) 
        {
            char[] ch = str.toCharArray();
            Arrays.sort(ch);

            String sortedKey = new String(ch);

            map.computeIfAbsent(sortedKey, k -> new ArrayList<>()).add(str);
        }

        List<List<String>> ans = new ArrayList<>(map.values());

        return ans;
        
    }
}