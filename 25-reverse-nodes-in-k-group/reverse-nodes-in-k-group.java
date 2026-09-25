class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode prevKEnd = dummy;
        ListNode oldHead = head;

        ListNode kth = getKthNode(oldHead, k);

        while(kth != null)
        {
            ListNode nextKStart = kth.next;

            //cut current group
            kth.next = null;

            //reverse current group
            ListNode newHead = reverse(oldHead);

            //connect previous group -> current reversed group
            prevKEnd.next = newHead;

            //connect current reversed group -> next group
            oldHead.next = nextKStart;

            //move pointers for next group
            prevKEnd = oldHead;
            oldHead = nextKStart;

            kth = getKthNode(oldHead, k);
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

    private ListNode getKthNode(ListNode node, int k)
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