class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        gatherSubsets(nums, 0, temp, ans);
        return ans;
    }

    private void gatherSubsets(int[] nums, int index, List<Integer> temp, List<List<Integer>> ans)
    {
        ans.add(new ArrayList<>(temp));

        for(int i = index; i < nums.length; i++)
        {
            temp.add(nums[i]);
            gatherSubsets(nums, i + 1, temp, ans);
            temp.remove(temp.size() - 1);
        }
    }
}