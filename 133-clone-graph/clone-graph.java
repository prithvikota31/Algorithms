/*
// Definition for a Node.
class Node {
    public int val;
    public List<Node> neighbors;
    public Node() {
        val = 0;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<Node>();
    }
    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}
*/

class Solution {
    public Node cloneGraph(Node node) {
        if(node == null)    return null;
        Map<Node, Node> map = new HashMap<>();

        //bfs
        Deque<Node> q = new ArrayDeque<>();
        q.offer(node);
        map.put(node, new Node(node.val));

        while(!q.isEmpty())
        {
            Node curr = q.poll();

            for(Node n: curr.neighbors)
            {   
                if(!map.containsKey(n))
                {
                    q.offer(n);
                    map.put(n, new Node(n.val));
                }
                map.get(curr).neighbors.add(map.get(n));
            }
        }

        return map.get(node);
    }
}