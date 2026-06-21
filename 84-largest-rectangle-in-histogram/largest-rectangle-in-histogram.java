class Solution {
    public int largestRectangleArea(int[] heights) {
        Stack<Integer> st = new Stack<>();
        int maxArea = 0;

        for(int i = 0; i <= heights.length; i++)
        {
            int currHt = (i == heights.length) ? 0 : heights[i];

            while(!st.isEmpty() && heights[st.peek()] > currHt)
            {
                int h = heights[st.pop()];
                int right = i;
                int left = st.isEmpty() ? -1 : st.peek();

                int w = right - left - 1;

                maxArea = Math.max(maxArea, h*w);
            }

            st.push(i);

        }

        return maxArea;

    }
}