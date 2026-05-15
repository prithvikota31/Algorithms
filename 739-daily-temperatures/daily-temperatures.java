class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        //maintain a decreasing stack of indices
        Stack<Integer> stack = new Stack<>();
        int[] ans = new int[temperatures.length];
        for(int i = 0; i < temperatures.length; i++)
        {
            while(!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i])
            {
                int top = stack.pop();
                ans[top] = i - top; 
            }
            stack.push(i);        
        }

        return ans;
    }
}