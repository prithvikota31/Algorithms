class MedianFinder {

    private PriorityQueue<Integer> leftMinHeap = new PriorityQueue<>();
    private PriorityQueue<Integer> rightMaxHeap = new PriorityQueue<>((a, b) -> (b - a));

    public MedianFinder() {
        
    }
    
    //for me left can have same or one more than a
    public void addNum(int num) {
        leftMinHeap.offer(num);
        rightMaxHeap.offer(leftMinHeap.poll());
        if(rightMaxHeap.size() > leftMinHeap.size())
        {
            leftMinHeap.offer(rightMaxHeap.poll());
        }
    }
    
    public double findMedian() {
        if(rightMaxHeap.size() == leftMinHeap.size())
        {
            return (double)(rightMaxHeap.peek() + leftMinHeap.peek()) / 2.0;
        }
        else
        {
            return leftMinHeap.peek();
        }
    }
}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */