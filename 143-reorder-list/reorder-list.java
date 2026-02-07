/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public void reorderList(ListNode head) {
        ListNode midNode = middleNode(head);
        ListNode second = reverseList(midNode.next);
        midNode.next = null;


        // Merge alternately (IN-PLACE)
        ListNode first = head;
        while (second != null && first != null) {
            ListNode t1 = first.next; //temp1
            ListNode t2 = second.next; //temp2

            first.next = second;
            second.next = t1;

            first = t1;
            second = t2;
        }

    }


    public ListNode middleNode(ListNode head)
    {
        ListNode slow = head;
        ListNode fast = head;

        while(fast.next != null && fast.next.next != null) // this give first node if even number
        {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }


    public ListNode reverseList(ListNode head)
    {
        ListNode prevNode = null;
        ListNode curNode = head;

        while(curNode != null)
        {
            ListNode nextNode = curNode.next;
            curNode.next = prevNode;
            prevNode = curNode;
            curNode = nextNode;
        }

        return prevNode;
    }
}