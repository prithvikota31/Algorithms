class Solution {
    public List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        HashMap<Character, String> map = new HashMap<>();


        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");

        StringBuilder sb = new StringBuilder();

        gatherCombinations(digits, map, 0, sb, ans);

        return ans; 
    }

    private void gatherCombinations(String digits, HashMap<Character, String> map,
                                                         int ind,  StringBuilder sb, List<String> ans)
    {
        if(ind == digits.length())
        {
            ans.add(new String(sb));
            return;
        }
        char digit = digits.charAt(ind);

        String possibleLetters = map.get(digit);

        for(int i = 0; i < possibleLetters.length(); i++)
        {
            sb.append(possibleLetters.charAt(i));
            gatherCombinations(digits, map, ind + 1, sb, ans);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

}