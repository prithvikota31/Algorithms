class Solution {
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>(); //indices
        //we maintin a striclty increasing stack because when we encounter a lesser number
        // we calculate maxarea and the element below it is left side min
        int maxArea = 0;
        stack.offerLast(-1);
        for(int i = 0; i < heights.length; i++)
        {
            while(stack.peekLast() != -1 && heights[stack.peekLast()] >= heights[i])
            {
                //now caculate
                int rightSideMin = i;
                int curVal = heights[stack.pollLast()];
                int leftSideMin = stack.peekLast();
                maxArea = Math.max(maxArea, curVal * (rightSideMin - leftSideMin - 1));
            }
            stack.offerLast(i);
        }

        while(stack.peekLast() != -1)
        {
            int rightSideMin = heights.length;
            int curVal = heights[stack.pollLast()];
            int leftSideMin = stack.peekLast();
            

            maxArea = Math.max(maxArea, curVal * (rightSideMin - leftSideMin - 1));

        }




        return maxArea;
    }
}