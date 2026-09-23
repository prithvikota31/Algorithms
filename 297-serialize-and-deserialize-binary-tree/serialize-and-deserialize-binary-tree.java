/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        //, for separator, # for null
        StringBuilder sb = new StringBuilder();
        serializeDfs(root, sb);
        return sb.toString();
    }

    //use postorder, and same order while deserialization
    private void serializeDfs(TreeNode node, StringBuilder sb)
    {
        if(node == null)
        {
            sb.append("#,");
            return;
        }

        int val = node.val;
        sb.append(val).append(",");

        serializeDfs(node.left, sb);
        serializeDfs(node.right, sb);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] dataArray = data.split(",");
        int[] index = {0};
        return deserializeDfs(dataArray, index);
    }

    private TreeNode deserializeDfs(String[] dataArray, int[] index)
    {
        String val = dataArray[index[0]];
        index[0]++;

        if(val.equals("#"))
        {
            return null;
        }

        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = deserializeDfs(dataArray, index);
        node.right = deserializeDfs(dataArray, index);
        return node;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec ser = new Codec();
// Codec deser = new Codec();
// TreeNode ans = deser.deserialize(ser.serialize(root));