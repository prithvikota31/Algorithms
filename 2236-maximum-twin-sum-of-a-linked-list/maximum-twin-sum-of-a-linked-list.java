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
    public int pairSum(ListNode head) {
        ListNode head2 = getSecondHalfHead(head);
        head2 = reverseLL(head2);
        ListNode head1 = head;
        int maxPair = Integer.MIN_VALUE;
        while(head2 != null)
        {
            maxPair = Math.max(maxPair, head1.val + head2.val);
            head1 = head1.next;
            head2 = head2.next;
        }

        return maxPair;
    }

    private ListNode getSecondHalfHead(ListNode head)
    {
        //1 -> 2 -> 3 -> 4 -> 5 -> 6 (based on this example)
        ListNode slow = head;
        ListNode fast = head;
        while(fast.next != null && fast.next.next != null)
        {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow.next;
    }

    private ListNode reverseLL(ListNode head)
    {
        //0->1->2->3
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
}