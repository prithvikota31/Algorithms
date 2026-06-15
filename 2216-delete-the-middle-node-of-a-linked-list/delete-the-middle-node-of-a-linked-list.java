class Solution {
    public ListNode deleteMiddle(ListNode head) {
        // If there is only one node, deleting middle means list becomes empty.
        if (head == null || head.next == null) {
            return null;
        }

        ListNode slow = head;
        ListNode fast = head;
        ListNode prev = null;

        // Move fast by 2 and slow by 1.
        // When fast reaches the end, slow will be at the middle.
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        // slow is the middle node.
        // prev is the node before middle.
        prev.next = slow.next;

        return head;
    }
}