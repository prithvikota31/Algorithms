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
        //capture prevLast and nextFirst always
        //current list curHead capture 
        //reverse will give new head

        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode prevLast = dummy;
        ListNode kth = getKthNode(dummy, k);
        //dummy -> 1 -> 2 -> 3 -> null
        
        // ListNode nextFirst = kth.next; //4
        // ListNode cur = dummy;
        

        while(kth != null)
        {
            ListNode oldHead = prevLast.next;
            ListNode nextFirst = kth.next;
            kth.next = null;
            ListNode newHead = reverse(oldHead);

            prevLast.next = newHead; //dummy -> 3 -> 2 -> 1 -> 4
            oldHead.next = nextFirst;
            prevLast = oldHead;
            
            kth = getKthNode(prevLast, k);
        }


        return dummy.next;


    }

    //reverse a list

    private ListNode reverse(ListNode head)
    {
        ListNode prev = null;

        ListNode cur = head;
        while(cur != null)
        {
            ListNode temp = cur.next;
            cur.next = prev;
            prev = cur;
            cur = temp;
        }

        return prev;
    }

    private ListNode getKthNode(ListNode node, int k) //always give prevLast to get kth node
    {
        //1 -> 2 -> 3 -> 4 -> 5
        // k = 3
        //
        ListNode cur = node;
        while(k > 0 && cur != null) //cur = 4, k = 0
        {
            cur = cur.next;
            k--;
        }

        return cur;
    }
}