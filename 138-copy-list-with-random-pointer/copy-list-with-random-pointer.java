/*
// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
*/

class Solution {
    public Node copyRandomList(Node head) {
        Map<Node, Node> map = new HashMap<>();

        Node cur = head;
        while(cur != null)
        {
            if(!map.containsKey(cur))
            {
                map.put(cur, new Node(cur.val)); //created a copy of cur node if not present
            }
            //now mark cur.next and cur.random pointers in copies too
            Node curNext = cur.next;
            Node curRandom = cur.random;
            Node copy = map.get(cur);
            if(curNext == null)
            {
                copy.next = null;
            }
            else
            {
                if(!map.containsKey(curNext))
                {
                    map.put(curNext, new Node(curNext.val));
                }
                copy.next = map.get(curNext);
                
            }

            if(curRandom == null)
            {
                copy.random = null;
            }
            else
            {
                if(!map.containsKey(curRandom))
                {
                    map.put(curRandom, new Node(curRandom.val));
                }

                copy.random = map.get(curRandom);
            }
            cur = cur.next;
        }

        return map.get(head);
    }
}