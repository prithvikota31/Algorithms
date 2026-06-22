class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        //keep strictly decreasing stack
        Deque<Integer> stack = new ArrayDeque<>();
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