class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode groupPrev = dummy;

        ListNode kth = getKth(groupPrev, k);

        while (kth != null) {

            ListNode groupNext = kth.next;

            // Reverse current k-group
            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;

            while (curr != groupNext) {
                ListNode temp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = temp;
            }

            // Reconnect
            ListNode oldGroupHead = groupPrev.next;

            groupPrev.next = kth;

            groupPrev = oldGroupHead;

            // Move to next group
            kth = getKth(groupPrev, k);
        }

        return dummy.next;
    }

    private ListNode getKth(ListNode curr, int k) {

        while (curr != null && k > 0) {
            curr = curr.next;
            k--;
        }

        return curr;
    }
}