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
        ListNode middleNode = middleLL(head);
        ListNode secondHead = reverseLL(middleNode.next);
        middleNode.next = null;

        ListNode finalHead = new ListNode();
        ListNode dummy = finalHead;
        while(head != null && secondHead != null)
        {
            dummy.next = head;
            head = head.next;
            dummy = dummy.next;

            dummy.next = secondHead;
            secondHead = secondHead.next;
            dummy = dummy.next;
        }

        if(head != null)
        {
            dummy.next = head;
        }

        head = finalHead.next;
    }

    private ListNode reverseLL(ListNode head)
    {
        ListNode prev = null;
        ListNode cur = head;
        while(cur != null)
        {
            ListNode temp = cur.next;
            cur.next = prev;
            prev = cur;
            cur = temp;
        }

        return prev;
    }

    private ListNode middleLL(ListNode head) // gets first middle 
    {
        ListNode slow = head;
        ListNode fast = head;

        while(fast.next != null && fast.next.next != null)
        {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }
}