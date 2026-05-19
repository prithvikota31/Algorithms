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
        Tuple curTuple = reverse(head, k);
        ListNode result = curTuple.head;
        while(curTuple.nextNode != null)
        {
            Tuple tempTuple = reverse(curTuple.nextNode, k);
            curTuple.tail.next = tempTuple.head;
            curTuple = tempTuple;
        }

        return result;
    }

    public Tuple reverse(ListNode head, int k)
    {
        ListNode originalHead = head;
        ListNode prev = null;
        ListNode cur = head;

        for(int i = 0; i < k; i++)
        {
            if(cur != null)
            {
                cur = cur.next;
            }
            else
            {
                return new Tuple(head, null, null);
            }
        }
        cur = head;

        int i = 0;
        while(cur != null && i < k)
        {
            ListNode temp = cur.next;
            cur.next = prev;
            prev = cur;
            cur = temp;
            i++;
        }

        if(i == k)
        {
            return new Tuple(prev, originalHead, cur);
        }
        else
        {
            return new Tuple(head, null, null);
        }
    }
}

class Tuple{
    ListNode head;
    ListNode tail;
    ListNode nextNode;

    public Tuple(ListNode head, ListNode tail, ListNode nextNode)
    {
        this.head = head;
        this.tail = tail;
        this.nextNode = nextNode;
    }
}