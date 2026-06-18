class Solution {
    public List<String> letterCombinations(String digits) {

        if (digits == null || digits.length() == 0) {
            return new ArrayList<>();
        }
        
        HashMap<Character, String> map = new HashMap<>();

        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");

        List<String> ans = new ArrayList<>();
        backtrack(map, 0, digits, ans, new StringBuilder());

        return ans;
    }

    private void backtrack(HashMap<Character, String> map, int ind, String digits, List<String> ans, StringBuilder sb)
    {
        if(ind == digits.length())
        {
            ans.add(sb.toString());
            return;
        }

        char digit = digits.charAt(ind);
        String letters = map.get(digit);
        for(int i = 0; i < letters.length(); i++)
        {
            sb.append(letters.charAt(i));
            backtrack(map, ind + 1, digits, ans, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }


}