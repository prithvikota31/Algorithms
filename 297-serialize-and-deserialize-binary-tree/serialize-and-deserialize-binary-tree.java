public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeDfs(root, sb);
        return sb.toString();
    }

    private void serializeDfs(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("#,");
            return;
        }

        // Preorder: root -> left -> right
        sb.append(node.val).append(",");

        serializeDfs(node.left, sb);
        serializeDfs(node.right, sb);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] values = data.split(",");
        int[] index = {0};

        return deserializeDfs(values, index);
    }

    private TreeNode deserializeDfs(String[] values, int[] index) {
        String val = values[index[0]++];

        if (val.equals("#")) {
            return null;
        }

        TreeNode node = new TreeNode(Integer.parseInt(val));

        // Same preorder order used during serialization
        node.left = deserializeDfs(values, index);
        node.right = deserializeDfs(values, index);

        return node;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec ser = new Codec();
// Codec deser = new Codec();
// TreeNode ans = deser.deserialize(ser.serialize(root));