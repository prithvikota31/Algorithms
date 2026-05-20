class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(); // minHeap
        //when heap size increases greate than k, add and remove

        for(int i = 0; i < nums.length; i++)
        {
            pq.offer(nums[i]);

            if(pq.size() > k)
            {
                pq.poll();
            }
        }

        return pq.peek();
    }
}