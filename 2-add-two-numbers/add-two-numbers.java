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

 // 2, 4, 3
 // 5 , 6, 4
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //carry is always 1 or 0 for base-10 single digit addition
        //carry comes up from sum 10 to 18
        ListNode current = new ListNode();
        int carry = 0;
        ListNode head = current;



        while(l1 != null || l2 != null || carry != 0)
        {
            int sum = carry;
            if(l1 != null)  
            {
                sum += l1.val;
                l1 = l1.next;
            }
            if(l2 != null) 
            {
                sum += l2.val;
                l2 = l2.next;
            }
            carry = sum / 10;
            sum = sum % 10;
            
            current.next = new ListNode();  
            current= current.next;
            current.val = sum;
                          
        }

        return head.next;

    }
}