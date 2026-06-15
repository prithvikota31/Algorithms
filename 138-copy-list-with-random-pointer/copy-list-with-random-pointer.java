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
        if(head == null)
        {
            return null;
        }

        //interleave
        // A -> B -> C
        Node cur = head;
        while(cur != null)
        {
            Node temp = cur.next;
            Node copy = new Node(cur.val);
            cur.next = copy;
            copy.next = temp;
            cur = temp;
        }

        //connect random pointers
        cur = head;
       
        while(cur != null)
        {
            Node curCopy = cur.next;
            Node curRandom = cur.random;
            if(curRandom != null)
            {
                curCopy.random = curRandom.next;
            }
            cur = cur.next.next;
            
        }


        // Separate original and copied list
        cur = head;
        Node copyHead = head.next;

        while (cur != null) {
            Node copy = cur.next;       // copied node after current original
            Node nextOriginal = copy.next; // next original node

            // Restore original list
            cur.next = nextOriginal;

            // Connect copied list
            if (nextOriginal != null) {
                copy.next = nextOriginal.next;
            } else {
                copy.next = null;
            }

            // Move to next original node
            cur = nextOriginal;
        }

        return copyHead;
    }
}