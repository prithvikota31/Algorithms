class Solution {
    public boolean isValid(String s) {
       //()
       //({{})}

        //({
       //open =  1
       //openC = 2

       Deque<Character> stack = new ArrayDeque<>();

       //stack will have only open brackets ({
        //()}
        for(int i = 0; i < s.length(); i++)
        {
            
            if(s.charAt(i) == '(' || s.charAt(i) == '{' || s.charAt(i) == '[')
                stack.push(s.charAt(i));
            else
            {
                if(stack.isEmpty())  return false;

                if((s.charAt(i) == ')' && stack.peek() != '(') ||
                    (s.charAt(i) == '}' && stack.peek() != '{') ||
                    (s.charAt(i) == ']' && stack.peek() != '['))
                {
                    return false;
                }
                else
                {
                    stack.pop();
                }
            }
        }
        //(((())

        if(!stack.isEmpty())    return false;
        return true;
    }
}