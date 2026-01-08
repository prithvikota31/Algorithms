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
    private int kmin = 0;
    private int count = 0;
    public int kthSmallest(TreeNode root, int k) {
        inOrder(root, k);
        return kmin;
    }

    public void inOrder(TreeNode root, int k)
    {
        if(root == null || count >= k)    return;


        inOrder(root.left, k);
        count++;
        if(count == k)  {
            kmin = root.val;
            return;
        }
        inOrder(root.right, k);
    }
}