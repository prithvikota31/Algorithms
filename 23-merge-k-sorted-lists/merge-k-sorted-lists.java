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
        int k = lists.length;
        PriorityQueue<Pair> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
        for(int i = 0; i < k; i++)
        {
            if(lists[i] != null)
                pq.offer(new Pair(lists[i].val, lists[i]));
        }

        ListNode dummy = new ListNode();
        ListNode curr = dummy;
        while(!pq.isEmpty())
        {
            Pair temp = pq.poll();
            curr.next = temp.node;
            ListNode nextNode = temp.node.next;
            curr = curr.next;
            if(nextNode != null)
            {

                pq.offer(new Pair(nextNode.val, nextNode));
            }

        }

        return dummy.next;
    }
}

class Pair{
    int val;
    ListNode node;
    public Pair(int val, ListNode node)
    {
        this.val = val;
        this.node = node;
    }
}