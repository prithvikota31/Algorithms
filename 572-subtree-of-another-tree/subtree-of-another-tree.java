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
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        //go left and right
        if(subRoot == null)
        {
            return true;
        }
        if(root == null)
        {
            return false;
        }
        boolean current = false;
        if(root.val == subRoot.val)
        {
            current = isSameTree(root, subRoot);
        }
        
        boolean left = isSubtree(root.left, subRoot);
        boolean right = isSubtree(root.right, subRoot);

        return current || left || right;
    }

    private boolean isSameTree(TreeNode p, TreeNode q) {
        if(p == null && q == null)
        {
            return true;
        }

        if(p == null || q == null)
        {
            return false;
        }

        if(p.val != q.val)
        {
            return false;
        }

        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}