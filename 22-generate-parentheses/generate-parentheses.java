class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        int open = 0;
        int close = 0;

        backtrack(n, ans, sb, open, close);

        return ans;
    }

    public void backtrack(int n, List<String> ans, StringBuilder sb, int open, int close)
    {
        if(sb.length() == 2*n)
        {
            ans.add(sb.toString());
            return;
        }

        if(close < open)
        {
            sb.append(")");
            backtrack(n, ans, sb, open, close+1);
            sb.deleteCharAt(sb.length() - 1);
        }

        if(open < n)
        {
            sb.append("(");
            backtrack(n, ans, sb, open+1, close);
            sb.deleteCharAt(sb.length() - 1);
        }


    }
}