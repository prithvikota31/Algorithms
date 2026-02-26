
class LRUCache {
    Node head = new Node(0, 0), tail = new Node(0, 0);
    Map < Integer, Node > map = new HashMap();
    int capacity;

    public LRUCache(int _capacity) {
        capacity = _capacity;
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if(map.containsKey(key))
        {
            Node node = map.get(key);
            remove(node);
            insert(node);
            return node.value;         
        }
        else
        {
            return -1;
        }
    }

    public void put(int key, int value) {

        if(map.containsKey(key))
        {
            Node node = map.get(key);
            node.value = value;
            remove(node);
            insert(node);
        }
        else
        {
            if(map.size() == capacity)
            {
                Node lNode = tail.prev;
                remove(lNode);
                map.remove(lNode.key);
            }
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            insert(newNode);
        }       
    }

    private void remove(Node node) { // just remove
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
    }

    private void insert(Node node) { // insert at beginning
        Node headCurNext = head.next;
        head.next = node;
        node.prev = head;
        node.next = headCurNext;
        headCurNext.prev = node;
    }

    class Node {
        Node prev, next;
        int key, value;
        Node(int _key, int _value) {
            key = _key;
            value = _value;
        }
    }
}      


/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */