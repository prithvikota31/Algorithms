/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public boolean isValidBST(TreeNode root) {
        return isValidBSTHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValidBSTHelper(TreeNode root, long min, long max)
    {
        if(root == null)    return true;

        if(root.val <= min || root.val >= max)
        {
            return false;
        }

        boolean left = isValidBSTHelper(root.left, min, Math.min(max, root.val));
        boolean right = isValidBSTHelper(root.right, Math.max(min, root.val), max);

        return right && left;
    }
}