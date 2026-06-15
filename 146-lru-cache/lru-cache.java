class LRUCache {
    private Node head = new Node();
    private Node tail = new Node();
    Map<Integer, Node> map = new HashMap<>();
    int capacity;
    public LRUCache(int capacity) {
        head.next = tail;
        tail.prev = head;
        this.capacity = capacity;
    }
    
    public int get(int key) {
        //no capacity changed
        if(!map.containsKey(key))
        {
            return -1;
        }
        else
        {
            Node node = map.get(key);
            remove(node);
            insert(node);
            return node.val;
        }
    }
    
    public void put(int key, int value) {
        if(map.containsKey(key)) //do nothing to capacity -> capacity unchanged, 
        {
            Node node = map.get(key);
            node.val = value;
            remove(node);
            insert(node);     
        }
        else
        {
            Node node = new Node();
            node.key = key;
            node.val = value;

            insert(node);
            map.put(key, node);
        }

        if(map.size() > capacity)
        {
            Node lastNode = tail.prev;
            map.remove(lastNode.key);
            remove(lastNode);
        }
    }

    private void insert(Node node) //insert immediately after head
    {
        Node currentLatest = head.next;
        head.next = node;
        node.prev = head;

        node.next = currentLatest;
        currentLatest.prev = node;
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
}
class Node{
    int key;
    int val;
    Node prev;
    Node next;
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */