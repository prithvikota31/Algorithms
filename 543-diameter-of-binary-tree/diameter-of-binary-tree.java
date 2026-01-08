class Solution {
    int maxValue = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        height(root);          // fills maxValue
        return maxValue;       
    }

    private int height(TreeNode root) {
        if (root == null) return 0;

        int left = 0, right = 0;

        if (root.left != null)
            left = 1 + height(root.left);

        if (root.right != null)
            right = 1 + height(root.right);

        maxValue = Math.max(maxValue, left + right);
        return Math.max(left, right);
    }
}
