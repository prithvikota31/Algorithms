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
        if(head == null)    return null;
        //interleave

        Node cur = head;
        while(cur != null)
        {
            Node curNext = cur.next;
            Node copy = new Node(cur.val);
            cur.next = copy;
            copy.next = curNext;
            cur = curNext;
        }

        //set random for copy
        cur = head;
        while(cur != null)
        {
            Node curRandom = cur.random;
            Node copy = cur.next;
            if(curRandom != null)
            {
                copy.random = curRandom.next;
            }

            cur = cur.next.next;
        }

        // remove interleave
        Node copyHead = head.next;

        Node result = copyHead;
        cur = head;

        while(cur != null)
        {
            cur.next = copyHead.next;
            if(copyHead.next != null)
                copyHead.next = cur.next.next;

            cur = cur.next;
            copyHead = copyHead.next;
        }

        return result;
    }
}