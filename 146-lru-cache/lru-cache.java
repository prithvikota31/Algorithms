class LRUCache {
    private int capacity;
    private Node head = new Node(-1, -1);
    private Node tail = new Node(-1, -1);
    private Map<Integer, Node> map = new HashMap<>();
    public LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        if(!map.containsKey(key))
        {
            return -1;
        }

        //no capacity change
        Node node = map.get(key);
        remove(node);
        add(node);
        return node.val;
    }
    
    public void put(int key, int value) {
        if(map.containsKey(key))
        {
            //update node value;
            //remove and add at start
            //dont touch hash map
            Node node = map.get(key);
            node.val = value;
            remove(node);
            add(node);
        }
        else
        {
            if(map.size() == capacity)
            {
                //capacity reched, remove node before tail
                map.remove(tail.prev.key);
                remove(tail.prev);
                
            }

            Node newNode = new Node(key, value);
            add(newNode);
            map.put(key, newNode);
        }
    }

    private void remove(Node node)
    {
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;

        node.next = null;
        node.prev = null;
    }

    private void add(Node node) // add at head
    {
        Node first = head.next;
        head.next = node;
        node.prev = head;

        node.next = first;
        first.prev = node;
    }
}

class Node{
    int key;
    int val;
    Node prev;
    Node next;

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