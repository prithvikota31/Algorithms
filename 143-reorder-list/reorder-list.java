class Solution {

    public void reorderList(ListNode head) {

        if(head == null || head.next == null) {
            return;
        }

        ListNode middle = middleOfLL(head);

        ListNode head2 = reverseLL(middle.next);
        middle.next = null;

        ListNode head1 = head;

        // DUMMY NODE
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        while(head1 != null && head2 != null) {

            // attach from first half
            tail.next = head1;
            head1 = head1.next;
            tail = tail.next;

            // attach from second half
            tail.next = head2;
            head2 = head2.next;
            tail = tail.next;
        }

        // leftover node (odd length case)
        if(head1 != null) {
            tail.next = head1;
        }
    }

    public ListNode middleOfLL(ListNode head) {

        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    public ListNode reverseLL(ListNode head) {

        ListNode prev = null;
        ListNode curr = head;

        while(curr != null) {

            ListNode next = curr.next;

            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;
    }
}