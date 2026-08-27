class MinStack {

    //logic : if new min push s = 2 * val - min 
    Deque<Long> stack = new ArrayDeque<>();
    long min = Integer.MAX_VALUE;
    public MinStack() {
        
    }
    
    public void push(int value) {
        if(stack.isEmpty())
        {
            min = value;
            stack.push((long)value);
            return;
        }
        if(value < min) //eg 4 < 5
        {
            stack.push(2L * value - min); //3
            min = value; //4
        }
        else
        {
            stack.push((long)value);
        }
    }
    
    public void pop() {
        if(stack.isEmpty())
        {
            return;
        }

        long s = stack.peek();
        if(s >= min)
        {
            stack.pop();
            return;
        }
        else
        {
            min = 2 * min - s;
            stack.pop();
        }
    }
    
    public int top() {
        long s = stack.peek();
        if(s >= min)
        {
            return (int)s;
        }
        else
        {
            return (int)min;
        }
    }
    
    public int getMin() {
        return (int)min;
    }
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(value);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */