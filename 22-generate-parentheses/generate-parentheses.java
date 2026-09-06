class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();
        gatherCombinations(n, 0, 0, ans, new StringBuilder());
        return ans;

    }

    private void gatherCombinations(int n, int open, int close, List<String> ans, StringBuilder sb)
    {
        if(open + close == 2 * n)
        {
            ans.add(new String(sb));
            return;
        }

        if(open < n)
        {
            sb.append('(');
            gatherCombinations(n, open + 1, close, ans, sb);
            sb.deleteCharAt(sb.length() - 1);
        }

        if(close < open)
        {
            sb.append(')');
            gatherCombinations(n, open, close + 1, ans, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}