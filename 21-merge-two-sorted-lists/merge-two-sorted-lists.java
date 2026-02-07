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
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        if(list1 == null)   return list2;
        if(list2 == null)   return list1;
        //maintain list1 with small value
        if(list1.val > list2.val)   return mergeTwoLists(list2, list1);
        ListNode head = list1;

        //  1 2 5 4 
        //  2 3 4
        while(list1 != null && list2 != null)
        {
            if(list1.next == null)
            {
                list1.next = list2;
                break;
            }

            if(list1.next.val < list2.val)
            {
                list1 = list1.next;
            }

            else
            {
                ListNode temp = list2.next;
                list2.next = list1.next;
                list1.next = list2;
                list2 = temp;
                list1 = list1.next;
            }


        }


        return head;
    }
}