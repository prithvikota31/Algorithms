class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        collectSubsets(nums, temp, result, 0);
        return result;
    }

    public void collectSubsets(int[] nums, List<Integer> temp, 
                                    List<List<Integer>> result, int ind)
    {
        result.add(new ArrayList<>(temp));

        for(int i = ind; i < nums.length; i++)
        {
            temp.add(nums[i]);
            collectSubsets(nums, temp, result, i + 1);
            temp.remove(temp.size() - 1); //remove last
        }
    }
}