class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> trackingList = new ArrayList<>();
        findSubsets(result, 0, nums.length, trackingList, nums);
        return result;
    }

    public void findSubsets(List<List<Integer>> result, int index, int n, List<Integer> trackingList, int[] nums)
    {
        if(index == n)
        {
            List<Integer> temp = new ArrayList<>(trackingList);
            result.add(temp);
            return;
        }
        trackingList.add(nums[index]);
        findSubsets(result, index + 1, n, trackingList, nums);
        trackingList.remove(trackingList.size() - 1);
        findSubsets(result, index + 1, n, trackingList, nums);
    }
}