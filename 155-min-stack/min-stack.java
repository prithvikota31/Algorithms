class MinStack {
    Deque<Pair> stack ;
    int minimum;
    public MinStack() {
       stack = new ArrayDeque<>();
       minimum = Integer.MAX_VALUE;
    }
    
    public void push(int val) {
        minimum = Math.min(minimum, val);
        stack.push(new Pair(val, minimum));
    }
    
    public void pop() {
        stack.pop();
        if (stack.isEmpty()) minimum = Integer.MAX_VALUE;
        else minimum = stack.peek().min;
    }
    
    public int top() {
        return stack.peek().value;
    }
    
    public int getMin() {
        return minimum;
    }
}

class Pair
{
    int value;
    int min;

    public Pair(int v, int m)
    {
        value = v;
        min = m;
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