class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        //main heap size k, and top element is kth larget

        for(int num: nums)
        {
            minHeap.offer(num);
            if(minHeap.size() > k)
            {
                minHeap.poll();
            }
        }

        return minHeap.peek();
    }
}