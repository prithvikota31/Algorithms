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
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));

        for(ListNode node: lists)
        {
            if(node != null)
            {
                minHeap.offer(node);
            }
        }

        ListNode dummyNode = new ListNode();
        ListNode cur = dummyNode;
        while(!minHeap.isEmpty())
        {
            ListNode minNode = minHeap.poll();
            cur.next = minNode;
            if(minNode.next != null)
            {
                minHeap.offer(minNode.next);
            }
            cur = cur.next;
            
        }

        return dummyNode.next;
    }
}