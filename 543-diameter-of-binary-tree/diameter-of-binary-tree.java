class Solution {
    int diameter = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        height(root);
        return diameter;
    }

    private int height(TreeNode node) {
        if (node == null) return 0;

        int left = height(node.left);
        int right = height(node.right);

        // update diameter at this node
        diameter = Math.max(diameter, left + right);

        // return height
        return 1 + Math.max(left, right);
    }
}
