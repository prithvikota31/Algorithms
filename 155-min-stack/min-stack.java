class MinStack {
    
    Deque<Long> stack;
    long min; 
    public MinStack() {
        stack = new ArrayDeque<Long>();
    }
    
    public void push(int val) {
        if(stack.isEmpty())
        {
            stack.offerLast((long)val);
            min = val;
        }
        else if(val < min){
            stack.offerLast(2L*val - min);
            min = val;
        }
        else{
            stack.offerLast((long)val);
        }
    }
    
    public void pop() {
        long top = stack.pollLast();
        if(top < min)
        {
            min = 2L*min - top;
        }
    }
    
    public int top() {
        long top = stack.peekLast();
        if(top < min)   return (int)min;
        else    return (int)top;
    }
    
    public int getMin() {
        return (int)min;
    }
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(val);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */