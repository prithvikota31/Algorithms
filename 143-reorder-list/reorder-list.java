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
    public void reorderList(ListNode head) {
        ListNode first = head;
        ListNode midNode = middleNode(head);
        ListNode second = reverseList(midNode);

        // 1 2 -> 3
        //3 4 -> null 

        ListNode result = new ListNode(-1);
        boolean firstSwitch = true;
        while(second != null && first != null)
        {
            if(firstSwitch)
            {
                result.next = first;
                first = first.next;
                result = result.next;
            }
            else
            {
                result.next = second;
                second = second.next;
                result = result.next;
            }
            firstSwitch = !firstSwitch;

        }

        head = result.next;
    }


    public ListNode middleNode(ListNode head)
    {
        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null)
        {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }


    public ListNode reverseList(ListNode head)
    {
        ListNode prevNode = null;
        ListNode curNode = head;

        while(curNode != null)
        {
            ListNode nextNode = curNode.next;
            curNode.next = prevNode;
            prevNode = curNode;
            curNode = nextNode;
        }

        return prevNode;
    }
}