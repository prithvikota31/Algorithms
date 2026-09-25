class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        helper(n, ans, sb, 0, 0);

        return ans;  
    }

    public void helper(int n, List<String> ans, StringBuilder sb, int open, int close)
    {
        if(sb.length() == n * 2)
        {
            ans.add(sb.toString());
            return;
        }

        if(open < n)
        {
            sb.append("(");
            helper(n, ans, sb, open + 1, close);
            sb.deleteCharAt(sb.length() - 1);
        }

        if(close < open)
        {
            sb.append(")");
            helper(n, ans, sb, open, close + 1);
            sb.deleteCharAt(sb.length() - 1);
        }


    }
}