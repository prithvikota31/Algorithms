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
    public ListNode reverseList(ListNode head) {
        ListNode prevNode = null;
        ListNode cur = head;

        while(cur != null)
        {
            ListNode temp = prevNode;
            ListNode nextNode = cur.next;

            cur.next = prevNode;
            prevNode = cur;
            cur = nextNode;
        }
        return prevNode;
    }
}