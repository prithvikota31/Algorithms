class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] ans = new int[n - k + 1];
        //stores indices in decreasing order, 
        //so always queue front gives window maz
        Deque<Integer> queue = new ArrayDeque<>(); 
        int index = 0;
        for(int i = 0; i < nums.length; i++)
        {
            //remove rom front
            //first is front of deque
            while(!queue.isEmpty() && queue.peekFirst() <= i - k)
            {
                queue.pollFirst(); 
            }

            while(!queue.isEmpty() && nums[queue.peekLast()] < nums[i])
            {
                queue.pollLast();
            }

            queue.offer(i);

            if(i >= k - 1) //since window starts from k - 1
            {
                ans[index++] = nums[queue.peekFirst()];
            }
        }

        return ans;
    }
}