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

        Node dummy = head;

        while(dummy != null)
        {
            dummy.next = new Node(dummy.val, dummy.next);
            dummy = dummy.next.next;

        }

        dummy = head;

        while(dummy != null)
        {
            if(dummy.random != null)
            {
                dummy.next.random = dummy.random.next;
            }

            if(dummy.next != null)
            {
                dummy = dummy.next.next;
            } 
        }

       
        dummy = head;
        Node newList = new Node(0);
        Node temp = newList;

        while(dummy != null)
        {
            Node copy = dummy.next;
            dummy.next = dummy.next.next;

            temp.next = copy;
            temp = temp.next;

            dummy = dummy.next;
        }


        return newList.next;



        
        
    }
}