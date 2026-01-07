// class Solution {
//     public int evalRPN(String[] tokens) {
//         Deque<Integer> stack = new ArrayDeque<>();
//         for(String s: tokens)
//         {

//             if(s.equals("*") || s.equals("/") || s.equals("-") || s.equals("+"))
//             {
//                 int op1 = stack.pop();
//                 int op2 = stack.pop();
//                 int result;
//                 switch(s){
//                     case "*":
//                         result = op2 * op1;
//                         stack.push(result);
//                         break;
//                     case "+":
//                         result = op2 + op1;
//                         stack.push(result);
//                         break;
//                     case "-":
//                         result = op2 - op1;
//                         stack.push(result);
//                     break;
//                     default:
//                         result = op2 / op1;
//                         stack.push(result);
//                 }
//             }
//             else
//             {
//                 stack.push(Integer.valueOf(s));
//             }
//         }

//         return stack.peek();
//     }
// }

class Solution {
    public int evalRPN(String[] tokens) {
        Deque<Integer> st = new ArrayDeque<>();

        for (String t : tokens) {
            switch (t) {
                case "+" -> st.push(st.pop() + st.pop());
                case "-" -> {
                    int b = st.pop(), a = st.pop();
                    st.push(a - b);
                }
                case "*" -> st.push(st.pop() * st.pop());
                case "/" -> {
                    int b = st.pop(), a = st.pop();
                    st.push(a / b);
                }
                default -> st.push(Integer.parseInt(t));
            }
        }
        return st.pop();
    }

}