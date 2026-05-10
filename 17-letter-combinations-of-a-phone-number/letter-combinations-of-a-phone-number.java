class Solution {
    public List<String> letterCombinations(String digits) {
        Map<Character, String> map = new HashMap<>();

        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");

        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        getCombinations(result, sb, digits, 0, map);
        return result;

    }

    public void getCombinations(List<String> result, StringBuilder sb, String digits, int ind,
                                 Map<Character, String> map)
    {
        if(ind == digits.length())
        {
            result.add(sb.toString());
            return;
        }

        char charAtIndex = digits.charAt(ind);

        String digitCombinations = map.get(charAtIndex);

        for(int i = 0; i < digitCombinations.length(); i++)
        {
            sb.append(digitCombinations.charAt(i));
            getCombinations(result, sb, digits, ind + 1, map);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}