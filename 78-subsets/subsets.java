class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        getSubsets(ans, 0, nums, new ArrayList<>());
        return ans;
    }

    public void getSubsets(List<List<Integer>> ans, int index, int[] nums, List<Integer> trackingList)
    {
        if(index == nums.length)
        {
            ans.add(new ArrayList<>(trackingList));
            return;
        }
        trackingList.add(nums[index]);
        getSubsets(ans, index + 1, nums, trackingList);
        trackingList.remove(trackingList.size() - 1);
        getSubsets(ans, index + 1, nums, trackingList);

    }


}