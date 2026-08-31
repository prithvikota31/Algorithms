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
        ListNode dummy = new ListNode(-1);
        ListNode cur = head;

        ListNode prevKEnd = dummy;
        ListNode oldHead = head;
        ListNode kth = getKthNode(head, k);
        ListNode nextKStart = kth.next;
        kth.next = null;
        
        while(kth != null)
        {
            ListNode newHead = reverse(oldHead);
            oldHead.next = nextKStart;
            prevKEnd.next = newHead;

            prevKEnd = oldHead;
            oldHead = nextKStart;
            kth = getKthNode(oldHead, k);
            if(kth != null)
            {
                nextKStart = kth.next;
                kth.next = null;
            }
            
        }

        return dummy.next;


    }

    private ListNode reverse(ListNode node)
    {
        ListNode prev = null;
        ListNode cur = node;
        while(cur != null)
        {
            ListNode curNext = cur.next;
            cur.next = prev;
            prev = cur;
            cur = curNext;
        }
        return prev;
    }

    private ListNode getKthNode(ListNode node, int k) // give group first node. that gives kth node
    {
        for(int i = 1; i < k; i++)
        {
            if(node == null)
            {
                return null;
            }
            node = node.next;
        }
        return node;
    }
}