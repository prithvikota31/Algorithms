class MedianFinder {

    private PriorityQueue<Integer> leftMaxHeap;
    private PriorityQueue<Integer> rightMinHeap;

    //always maintain size(leftMaxHeap - rightMinHeap) <= 1

    public MedianFinder() {
        leftMaxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        rightMinHeap = new PriorityQueue<>();
    }
    
    public void addNum(int num) {
        leftMaxHeap.offer(num);
        rightMinHeap.offer(leftMaxHeap.poll());
        if(rightMinHeap.size() > leftMaxHeap.size()) //right == left and left > right ok (this maintains difference of 1)
        {
            leftMaxHeap.offer(rightMinHeap.poll());
        }
    }
    
    public double findMedian() {
        if(leftMaxHeap.size() == rightMinHeap.size())
        {
            return (leftMaxHeap.peek() + rightMinHeap.peek()) / 2.0;

        }
        else
            return (double)leftMaxHeap.peek();
    }
}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */