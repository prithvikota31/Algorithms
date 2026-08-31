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
    private int result = -1;
    private int count = 0;
    public int kthSmallest(TreeNode root, int k) {
        //do inorder, at kth element just return

        inOrder(root, k);
        return result;
    }

    private void inOrder(TreeNode node, int k)
    {
        if(node == null)    return;
        inOrder(node.left, k);
        count++;
        if(count == k)
        {
            result = node.val;
            return;
        }
        inOrder(node.right, k);
    }
}