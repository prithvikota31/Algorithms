/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */

class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //  1 - 1
        //2
        //3

        //    1
        // 2     3
        //4 5   6  7 
        //        8 9

        // 4  7 
        if(root == null)    return null;
        if(root.val == p.val || root.val == q.val)  return root;

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        //left right
        // x   x
        //null x
        // x null = x
        // null null = null

        if(left != null && right != null)   return root;
        else if(left == null && right == null)   return null;
        else if(left != null)   return left;
        else return right;
    }
}