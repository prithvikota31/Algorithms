class Solution {
    public boolean isValid(String s) {
        int n = s.length();
        if(n % 2 != 0)  return false;

        Deque<Character> stack = new ArrayDeque<>();

        for(int i = 0; i < s.length(); i++)
        {
            char ch = s.charAt(i);

            if(ch == '(' || ch == '{' || ch == '[')
            {
                stack.offerLast(ch);
            }
            else
            {
                if(stack.isEmpty())    return false;
                char cur = stack.pollLast();
                if((ch == ')' && cur == '(') || (ch == ']' && cur == '[') || (ch == '}' && cur == '{'))
                {
                    continue;
                }
                else
                {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }
}