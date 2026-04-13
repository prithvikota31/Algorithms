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
    boolean ans;
    public boolean isBalanced(TreeNode root) {
        ans = true;
        height(root);
        return ans;
    }

    public int height(TreeNode root)
    {
        if(root == null)    return 0;

        int left = 1 + height(root.left);
        int right = 1 + height(root.right);

        if(Math.abs(right - left) > 1)
        {
            ans = false;
        }

        return Math.max(left, right);
    }
}