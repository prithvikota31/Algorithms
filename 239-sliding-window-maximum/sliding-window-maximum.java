class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        int[] result = new int[nums.length - k + 1];
        int j = 0;
        //maintain a monotnic decreasing queue
        for(int i = 0; i < nums.length; i++)
        {
            //remove elements in the front
            while(!deque.isEmpty() && deque.peekFirst() <= i - k)
            {
                deque.pollFirst();
            }
            //remove element which are less than current

            while(!deque.isEmpty() && nums[deque.peekLast()] <= nums[i])
            {
                deque.pollLast();
            }

            deque.offerLast(i);

            if(i >= k - 1)
                result[j++] = nums[deque.peekFirst()];
        }

        return result;
    }
}