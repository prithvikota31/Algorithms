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
        

        ListNode curNode = head;
        ListNode prevNode = null;

        while(curNode != null)
        {
            ListNode next = curNode.next;
            curNode.next = prevNode;
            prevNode = curNode;
            curNode = next;
        }

        return prevNode;
    }
}