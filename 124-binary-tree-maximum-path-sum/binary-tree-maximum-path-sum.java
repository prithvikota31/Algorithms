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

    private int maxSum = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {

        findMaxPath(root);
        
        return maxSum;
    }

    private int findMaxPath(TreeNode root)
    {
        //nroot = null -> 0
        // root= leaf -> leaf.val
        //  1
        //2   3
        // computation for maxSum, with current root in the path, update a value outside
        // both sides +ve, root, +ve = sum = (all)
        // one side -ve, remaing(root + +ve)
        // both sides -ve, root
        // 
        // 
        if(root == null)    return Integer.MIN_VALUE;
        maxSum = Math.max(root.val, maxSum); //maxSum = -2-> -1
        if(isLeaf(root))    return root.val;
        //    -2
        // -1 
        
        int left =  findMaxPath(root.left);
        maxSum = Math.max(left, maxSum); //maxSum = -1
        if(left < 0)    left = 0;
        int right = findMaxPath(root.right); //0
        maxSum = Math.max(right, maxSum);
        if(right < 0)   right = 0;
        maxSum = Math.max(maxSum, root.val + left + right);



        return Math.max(Math.max(root.val, root.val + left), root.val + right);
    }

    private boolean isLeaf(TreeNode root)
    {
        return root.left == null && root.right == null;
    }
}