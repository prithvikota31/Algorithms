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
    public ListNode reverseKGroup(ListNode head, int k) {
        //prevGroup -> reversegroup -> nextGroup
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prevGroupTail = dummy;
        ListNode kth = getKthNode(dummy, k);

        while(kth != null)
        {
            ListNode nextGroupHead = kth.next;
            kth.next = null;
            reverse(prevGroupTail.next); //return again gives kth node;
            //make connections
            //old head will be new prevgrouptail
            ListNode oldHead = prevGroupTail.next;
            prevGroupTail.next = kth;
            oldHead.next = nextGroupHead;
            prevGroupTail = oldHead;
            kth = getKthNode(prevGroupTail, k);
        }

        return dummy.next;

    }

    public ListNode getKthNode(ListNode node, int k) // send head prev noe to get kth node
    {
        ListNode cur = node;
        while(k > 0 & cur != null)
        {
            cur = cur.next;
            k--;
        }

        return cur;
    }


    public ListNode reverse(ListNode head)
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
}