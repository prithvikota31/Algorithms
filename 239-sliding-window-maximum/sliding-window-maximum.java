class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        //maintain a deque 

        //1, 2, 3, 4, 5
        //for size 5 and k = 3 we have 3 sized max window = n - k + 1
        int n = nums.length;
        int[] ans = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();
        //stores strictly decreasing order of num value indexes
        int ind = 0;
        for(int i = 0; i < nums.length; i++)
        {
            //poll from front if i - polled >= k
            //eg: 0, 1, 2, 3, 4
            // suppose k = 3
            // at i = 3, we can poll (i - k) (0)
            while(!deque.isEmpty() && deque.peekFirst() <= i - k)
            {
                deque.pollFirst();
            }

            //poll from behind, to maintain strictly decreasing queue
            //eg: 5, 6, 8 to be inserted
            while(!deque.isEmpty() && nums[deque.peekLast()] <= nums[i])
            {
                deque.pollLast();
            }

            deque.offerLast(i);

            if(i >= k - 1)
            {
                ans[ind++] = nums[deque.peekFirst()];
            }
        }
        return ans;
    }
}