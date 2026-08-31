class Solution {
    private int count = 0;
    private int ans = -1;

    public int kthSmallest(TreeNode root, int k) {
        inOrder(root, k);
        return ans;
    }

    private boolean inOrder(TreeNode root, int k) {
        if (root == null) return false;

        // If answer was found in left subtree, propagate true upward immediately.
        if (inOrder(root.left, k)) return true;

        count++;

        if (count == k) {
            ans = root.val;
            return true;
        }

        // Propagate "found" through every recursive caller.
        return inOrder(root.right, k);
    }
}