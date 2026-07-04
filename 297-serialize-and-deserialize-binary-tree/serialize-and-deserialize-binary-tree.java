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
        if(root == null)    return "";
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while(!q.isEmpty())
        {
            TreeNode cur = q.poll();
            if(cur == null)
            {
                sb.append("#,");
                continue;
            }
            sb.append(cur.val + ",");
            q.offer(cur.left);
            q.offer(cur.right);
        }

        return sb.toString();
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if(data == "")  return null;

        String[] values = data.split(",");
        int rootVal = Integer.parseInt(values[0]);

        TreeNode root = new TreeNode(rootVal);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        for(int i = 1; i < values.length; i++)
        {
            TreeNode cur = q.poll();
            if(!values[i].equals("#"))
            {
                cur.left = new TreeNode(Integer.parseInt(values[i]));
                q.offer(cur.left);
            }
            i++;
            if(!values[i].equals("#"))
            {
                cur.right = new TreeNode(Integer.parseInt(values[i]));
                q.offer(cur.right);
            }
        }

        return root;

    }
}

// Your Codec object will be instantiated and called as such:
// Codec ser = new Codec();
// Codec deser = new Codec();
// TreeNode ans = deser.deserialize(ser.serialize(root));