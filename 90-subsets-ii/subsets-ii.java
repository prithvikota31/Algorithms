class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();

        findSubsets(nums, 0, ans, new ArrayList<>());

        return ans;
    }
    private void findSubsets(int[] nums, int start, List<List<Integer>> ans, List<Integer> trackingList)
    {
        ans.add(new ArrayList<>(trackingList));

        for(int i = start; i < nums.length; i++)
        {
            if(i > start && nums[i] == nums[i - 1])  continue;
            trackingList.add(nums[i]);
            findSubsets(nums, i + 1, ans, trackingList);
            trackingList.remove(trackingList.size() - 1);
        }
    }
}