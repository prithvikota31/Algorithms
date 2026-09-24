class Solution {
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        int n = s.length();

        for(int i = 0; i < n; i++)
        {
            char ch = s.charAt(i);

            if(ch == '(' || ch == '[' || ch == '{')
            {
                stack.push(ch);
                continue;
            }

            //we ecnounted closing brace

            if(stack.isEmpty()) return false;

            char top = stack.pop();

            if(top == '(' && ch == ')') continue;
            if(top == '[' && ch == ']') continue;
            if(top == '{' && ch == '}') continue;

            return false;
        }

        return stack.isEmpty();

    }
}