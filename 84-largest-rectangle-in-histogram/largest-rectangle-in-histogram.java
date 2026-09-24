class Solution {
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;
        //maintain a monotonically increasing stack indices, when we encounter less valu, calculate area with the stack top height
        for(int i = 0; i < heights.length; i++)
        {
            while(!stack.isEmpty() && heights[stack.peek()] >= heights[i])
            {
                int rightIndex = i;
                int curHeight = heights[stack.pop()];
                int leftIndex = stack.isEmpty()? -1: stack.peek();
                int area = curHeight * (rightIndex - leftIndex - 1);
                maxArea = Math.max(maxArea, area);
            }

            stack.push(i);
        }

        //there might be some indexes left, which do not find right min
        //for them
        //int rightIndex = heights.length;

        while(!stack.isEmpty())
        {
            int rightIndex = heights.length;
            int curHeight = heights[stack.pop()];
            int leftIndex = stack.isEmpty()? -1: stack.peek();
            int area = curHeight * (rightIndex - leftIndex - 1);
            maxArea = Math.max(maxArea, area);
        }

        return maxArea;

    }
}