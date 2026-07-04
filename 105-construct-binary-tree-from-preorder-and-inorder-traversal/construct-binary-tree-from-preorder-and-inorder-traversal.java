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
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> inorderMap = new HashMap<>();
        //3, 9, 20, 15, 7 - pre
        // 9, 3, 15, 20, 7
        for(int i = 0; i < inorder.length; i++)
        {
            inorderMap.put(inorder[i], i);
        }

        return buildHelper(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1, inorderMap);
    }

    private TreeNode buildHelper(int[] preorder, int preStart, int preEnd, int[] inorder,
                                int inStart, int inEnd, Map<Integer, Integer> inorderMap)
    {
        if(preStart > preEnd || inStart > inEnd)
        {
            return null;
        }

        //3, 9, 20, 15, 7 - pre
        // 9, 3, 15, 20, 7
        // 0, 1, 2, 3, 4
        //inpos = 1

        TreeNode root = new TreeNode(preorder[preStart]);
        int inRootPosition = inorderMap.get(preorder[preStart]);

        int numsLeft= inRootPosition - inStart;

        root.left = buildHelper(preorder, preStart + 1, preStart + numsLeft, inorder,
                                                         inStart, inRootPosition - 1, inorderMap);
        root.right = buildHelper(preorder, preStart + numsLeft + 1, preEnd, inorder,
                                                         inRootPosition + 1, inEnd, inorderMap);

        return root;
    }
}