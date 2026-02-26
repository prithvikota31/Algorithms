class LRUCache {
    private Node dummyStart;
    private Node dummyEnd;
    private int capacity;
    private int count;
    private Map<Integer, Node> map;
    public LRUCache(int capacity) {
        dummyStart = new Node(-1, -1);
        dummyEnd = new Node(-1, -1);
        this.capacity = capacity;

        dummyStart.next = dummyEnd;
        dummyEnd.prev = dummyStart;
        count = 0;
        map = new HashMap<>();
    }
    
    public int get(int key) {
        if(map.containsKey(key))
        {
           int val = map.get(key).val;
            //update cache
            remove(key);
            put(key, val);

           return val;
        }
        else    return -1;
    }
    
    public void put(int key, int value) {
        if(map.containsKey(key)) // true
        {
            remove(key);
        }
        addToCache(key, value);
    }

    private void addToCache(int key, int val)
    {
        Node node = new Node(key, val);
        Node first = dummyStart.next;
        first.prev = node; 
        node.next = first;
        node.prev = dummyStart;
        dummyStart.next = node;
        map.put(key, node);

        //increment count;
        count++;
        if(count > capacity)
        {
            remove(dummyEnd.prev.key);
        }
    }

    // private void evict()
    // {
    //     Node last = dummyEnd.prev;
    //     dummyEnd.prev = dummyEnd.prev.prev;
    //     dummyEnd.prev.next = dummyEnd;
    //     last.next = null;
    //     last.prev = null;

    //     count--;
    // }

    private void remove(int key)
    {
        Node node = map.get(key);
        Node prev = node.prev;
        Node next = node.next;

        prev.next = next;
        next.prev = prev;

        node.next = null;
        node.prev = null;
        map.remove(key);
        count--;
    }
}
class Node{
    int key;
    int val;
    Node next;
    Node prev;

    public Node(int key, int val)
    {
        this.key = key;
        this.val = val;
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */