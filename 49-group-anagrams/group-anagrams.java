class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        
        Map<String, List<String>> map = new HashMap<>();
        // the above hashmap contains sorted key to actual strs

        for(String str: strs)
        {
            char[] ch = str.toCharArray();
            Arrays.sort(ch);
            String sortedKey = new String(ch);

            //map.putIfAbsent
            if(!map.containsKey(sortedKey))
            {
                map.put(sortedKey, new ArrayList<>());
            }

            map.get(sortedKey).add(str);

        }


        return new ArrayList<>(map.values());
        
        
    }
}