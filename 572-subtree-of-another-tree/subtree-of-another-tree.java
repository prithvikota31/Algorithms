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
        List<TreeNode> matching = new ArrayList<>();
        preOrderTraversal(root, matching, subRoot.val);
        boolean result = false;
        for(int i = 0; i < matching.size(); i++)
        {
            result = result || isSameTree(matching.get(i), subRoot);
            if(result == true)  return true;
        }

        return false;

    }

    private void preOrderTraversal(TreeNode root, List<TreeNode> matching, int val)
    {
        if(root == null)    return;
        if(root.val == val) matching.add(root);

        if(root.left != null)   preOrderTraversal(root.left, matching, val);
        if(root.right != null)   preOrderTraversal(root.right, matching, val);
    }

    private boolean isSameTree(TreeNode root1, TreeNode root2)
    {
        if(root1 == null && root2 == null)  return true;
        else if(root1 == null || root2 == null) return false;

        if(root1.val != root2.val)  return false;

        return isSameTree(root1.left, root2.left) &&  isSameTree(root1.right, root2.right);

    }
}