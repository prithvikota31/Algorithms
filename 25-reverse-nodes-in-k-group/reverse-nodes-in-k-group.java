class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode prevKEnd = dummy;
        ListNode oldHead = head;

        while (oldHead != null) {

            ListNode kth = getKthNode(oldHead, k);

            // fewer than k nodes left -> leave them unchanged
            if (kth == null) {
                prevKEnd.next = oldHead;
                break;
            }

            ListNode nextKStart = kth.next;

            // cut current group
            kth.next = null;

            // reverse current group
            ListNode newHead = reverse(oldHead);

            // reconnect
            prevKEnd.next = newHead;
            oldHead.next = nextKStart;

            // move to next group
            prevKEnd = oldHead;
            oldHead = nextKStart;
        }

        return dummy.next;
    }

    private ListNode reverse(ListNode node) {
        ListNode prev = null;
        ListNode cur = node;

        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        return prev;
    }

    private ListNode getKthNode(ListNode node, int k) {
        for (int i = 1; i < k; i++) {
            if (node == null) {
                return null;
            }
            node = node.next;
        }

        return node;
    }
}