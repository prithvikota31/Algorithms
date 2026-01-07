// class MinStack {
//     Deque<Pair> stack ;
//     int minimum;
//     public MinStack() {
//        stack = new ArrayDeque<>();
//        minimum = Integer.MAX_VALUE;
//     }
    
//     public void push(int val) {
//         minimum = Math.min(minimum, val);
//         stack.push(new Pair(val, minimum));
//     }
    
//     public void pop() {
//         stack.pop();
//         if (stack.isEmpty()) minimum = Integer.MAX_VALUE;
//         else minimum = stack.peek().min;
//     }
    
//     public int top() {
//         return stack.peek().value;
//     }
    
//     public int getMin() {
//         return minimum;
//     }
// }

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

class MinStack {
    Deque<Long> stack;
    long min;

    public MinStack() {
        stack = new ArrayDeque<>();
    }

    public void push(int val) {
        if (stack.isEmpty()) {
            stack.push((long) val);
            min = val;
        } else if (val < min) {
            stack.push(2L * val - min); // encode
            min = val;
        } else {
            stack.push((long) val);
        }
    }

    public void pop() {
        long top = stack.pop();
        if (top < min) {
            min = 2 * min - top; // restore previous min
        }
    }

    public int top() {
        long top = stack.peek();
        return top < min ? (int) min : (int) top;
    }

    public int getMin() {
        return (int) min;
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