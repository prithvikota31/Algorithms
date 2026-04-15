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
        int[] result = new int[1];
        result[0]++;
        dfs(root, result, root.val);
        return result[0];
    }

    private void dfs(TreeNode root, int[] result, int maxSoFar)
    {
        if(root == null)    return;

        if(root.left != null)
        {
            if(root.left.val >=  maxSoFar)
            {
                result[0]++;
                dfs(root.left, result, root.left.val);
            }
            else
            {
                dfs(root.left, result, maxSoFar);
            }    
        }

        if(root.right != null)
        {
            if(root.right.val >=  maxSoFar)
            {
                result[0]++;
                dfs(root.right, result, root.right.val);
            }
            else
            {
                dfs(root.right, result, maxSoFar);
            }    
        }


    }
}