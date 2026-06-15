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
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }

        return divide(lists, 0, lists.length - 1);
    }

    private ListNode divide(ListNode[] lists, int left, int right) //divide lists
    {
        if(left == right)
        {
            return lists[left];
        }

        int mid = left + (right - left) / 2;

        ListNode leftList = divide(lists, left, mid);
        ListNode rightList = divide(lists, mid + 1, right);

        return mergeTwoLists(leftList, rightList);
    }


    private ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode finalNode = new ListNode();
        ListNode dummy = finalNode;

        while(list1 != null && list2 != null)
        {
            if(list1.val < list2.val)
            {
                dummy.next = list1;
                list1 = list1.next;
            }
            else
            {
                dummy.next = list2;
                list2 = list2.next;
            }

            dummy = dummy.next;
        }

        if(list1 == null)
        {
            dummy.next = list2;
        }
        else
        {
            dummy.next = list1;
        }

        return finalNode.next;
    }
}