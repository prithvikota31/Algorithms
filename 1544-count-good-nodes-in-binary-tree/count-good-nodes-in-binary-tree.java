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
    public int goodNodes(TreeNode root) {
        //take max in the path
        if(root == null)    return 0;
        return countGood(root, root.val);
    }

    public int countGood(TreeNode node, int max)
    {
        if(node == null)    return 0;
        int count = 0;
        if(node.val >= max) count++;
        int left = countGood(node.left, Math.max(max, node.val));
        count += left;
        int right = countGood(node.right, Math.max(max, node.val));
        count += right;

        return count;
    }
}