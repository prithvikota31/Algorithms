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
    private int count = 0;
    public int goodNodes(TreeNode root) {
        return  countGoodNodes(root, Integer.MIN_VALUE);
    }

    private int countGoodNodes(TreeNode node, int pathMax)
    {
        if(node == null)    return 0;
        int count = 0;
        if(node.val >= pathMax)  count = 1;
        count += countGoodNodes(node.left, Math.max(pathMax, node.val));
        count += countGoodNodes(node.right, Math.max(pathMax, node.val));

        return count;

    }
}