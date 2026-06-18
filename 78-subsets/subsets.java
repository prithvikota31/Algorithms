class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> curr = new ArrayList<>();
        dfs(nums, ans, curr, 0);

        return ans;
     
    }

    public void dfs(int[] nums, List<List<Integer>> ans, List<Integer> curr, int index)
    {
        if(index == nums.length)
        {
            ans.add(new ArrayList<>(curr));
            return;
        }
        curr.add(nums[index]);
        dfs(nums, ans, curr, index+1);

        curr.remove(curr.size() - 1);
        
        dfs(nums, ans, curr, index+1);
    }
}