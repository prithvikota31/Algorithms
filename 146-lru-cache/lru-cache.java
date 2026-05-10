class LRUCache {
    
    private int capacity;
    private int count;
    private Node head;
    private Node tail;
    private Map<Integer, Node> map;
    public LRUCache(int capacity) {
        this.capacity = capacity;
        head = new Node(-1, -1);
        tail = new Node(-1, -1);
        map = new HashMap<>();
        head.next = tail;
        head.prev = null;
        tail.prev = head;
        tail.next = null;
    }
    
    public int get(int key) {
        if(!map.containsKey(key))
        {
            return -1;
        }

        //remove the node
        Node node = remove(key, false);
        add(node);
        return node.val;
        //add at head (any addition always at head)

    }
    // 1 -> (2) -> 3
    public Node remove(int key, boolean permanent)
    {
        Node node = map.get(key);
        Node prev = node.prev;
        Node next = node.next;

        prev.next = next;
        next.prev = prev;
        node.prev = null;
        node.next = null;
        if(permanent)
        {
            map.remove(key);
            count--;
        }

        return node;
    }


    //-1 -> 1 -> 2
    public void add(Node node)
    {
        Node first = head.next; //1
        head.next = node;
        node.prev = head;
        node.next = first;
        first.prev = node;

    }
    
    public void put(int key, int value) {
        // key already exists
        if(map.containsKey(key))
        {
            remove(key, true);
        }

        Node node = new Node(key, value);

        map.put(key, node);        
        count++;
        add(map.get(key));
        if(count > capacity)
        {
            remove(tail.prev.key, true);
        }
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