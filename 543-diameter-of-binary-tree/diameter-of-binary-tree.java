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
    int maxValue = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        diameter(root);
        return maxValue;

    }

    public int diameter(TreeNode root)
    {
        if(isLeaf(root))    return 0;
        // 1
        //2 3  = 2
        int left = 0;
        int right = 0;
        if(root.left != null)
            left = 1 + diameter(root.left);
        if(root.right != null)
            right = 1 +  diameter(root.right);

        //  1
        //2
        maxValue = Math.max(maxValue, left + right);
        return Math.max(left, right);
    }

    public boolean isLeaf(TreeNode root)
    {
        return root.left == null && root.right == null;
    }
}