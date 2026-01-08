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
    private boolean checkBalance = true;
    public boolean isBalanced(TreeNode root) {
        //go with height
        height(root);
        return checkBalance;
    }

    private int height(TreeNode root)
    {
        if(root == null)    return 0;
        int left = height(root.left);
        int right = height(root.right);
        checkBalance = checkBalance && Math.abs(left - right) <= 1;
        return 1 + Math.max(left, right);

    }
}