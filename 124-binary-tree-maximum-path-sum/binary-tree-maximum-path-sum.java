class Solution {
    private int maxSum = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        dfs(root);
        return maxSum;
    }

    private int dfs(TreeNode root) {
        if (root == null) return 0;

        int left = Math.max(0, dfs(root.left));
        int right = Math.max(0, dfs(root.right));

        // best path using this node as the highest point
        maxSum = Math.max(maxSum, root.val + left + right);

        // return best downward path
        return root.val + Math.max(left, right);
    }
}
