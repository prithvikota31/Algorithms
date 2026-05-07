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
        ListNode ans = head;
        ListNode middle = middleOfLL(head);
        ListNode head2 = reverseLL(middle.next);
        middle.next = null;
        ListNode head1 = head;

        while(head1 != null && head2 != null)
        {
            ListNode t1 = head1.next;
            ListNode t2 = head2.next;

            head1.next = head2;
            head2.next = t1;

            head1 = t1;
            head2 = t2;

        } 
        
    }

    public ListNode middleOfLL(ListNode head)
    {
        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null)
        {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;

    }

     public ListNode reverseLL(ListNode head)
    {
        ListNode prev = null;
        ListNode curr = head;
        while(curr != null)
        {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;

    }
}