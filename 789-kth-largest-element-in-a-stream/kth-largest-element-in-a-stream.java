class KthLargest {
    private PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    private int k;
    //In minheap always maintain a size of k so that top element is the kth element


    public KthLargest(int k, int[] nums) {
        this.k = k;
        for(int num: nums)
        {
            add(num);
        }       
    }
    
    public int add(int val) {
        minHeap.offer(val);

        if(minHeap.size() > k)
        {
            minHeap.remove();
        }

        return minHeap.peek();
    }
}

/**
 * Your KthLargest object will be instantiated and called as such:
 * KthLargest obj = new KthLargest(k, nums);
 * int param_1 = obj.add(val);
 */