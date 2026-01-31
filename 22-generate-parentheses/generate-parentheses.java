class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dfs(sb, 0, 0, result, n);

        return result;
    }

    public void dfs(StringBuilder sb, int open, int close, List<String> result, int n)
    {
        if(sb.length() == 2 * n)
        {
            result.add(sb.toString());
            return;
        }

        if(open < n)
        {
            sb.append('(');
            dfs(sb, open + 1, close, result, n);
            sb.deleteCharAt(sb.length() - 1);
        }

        if(close < open)
        {
            sb.append(')');
            dfs(sb, open, close + 1, result, n);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}