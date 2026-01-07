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
        //will do DFS
        //since number of nodes not yet defined, define a set
        //hash is better, since we need to find node and extract neighbours
        if(node == null)    return null;

        Node clone = new Node(node.val);
        Map<Node, Node> visitedMap = new HashMap<>();
        visitedMap.put(node, clone);
        dfs(node, visitedMap);
        return clone;
    }
    

    public void dfs(Node node, Map<Node, Node> visitedMap)
    {
        for(Node n: node.neighbors) //1, 3, 4
        {
            if(!visitedMap.containsKey(n))
            {
                Node cloneNeighbor = new Node(n.val);
                visitedMap.put(n, cloneNeighbor);
                dfs(n, visitedMap);
            }
            visitedMap.get(node).neighbors.add(visitedMap.get(n));
        }
        
    }
}