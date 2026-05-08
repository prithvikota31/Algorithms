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
//1 -> 2 -> 3-> null
class Solution {
    public Node copyRandomList(Node head) {
        if(head == null)    return null;
        //first bring the copied next to original copy, interleave the nodes
        Node cur = head;
        while(cur != null)
        {
            Node curNext = cur.next;
            cur.next = new Node(cur.val);
            cur.next.next = curNext;
            cur = cur.next.next;
        }


        //random 
        cur = head;
        while(cur != null)
        {
            Node curRandom = cur.random;
            Node copyNode = cur.next;
            if(curRandom != null)
                copyNode.random = curRandom.next;

            cur = cur.next.next;
        }

        //remove the interleave
        Node copyHead = head.next;
        cur = head;
        Node result = copyHead;

        while(cur != null)
        {
            cur.next = cur.next.next;
            if(copyHead.next != null)
                copyHead.next = copyHead.next.next;

            cur = cur.next;
            copyHead = copyHead.next;
        }

        return result;
    }
}