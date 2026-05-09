class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        findSubsets(result, temp, nums, 0);
        return result;
    }


    public void findSubsets(List<List<Integer>> result, 
                            List<Integer> temp, int[] nums, int ind)
    {

        result.add(new ArrayList<>(temp));
        for(int i = ind; i < nums.length; i++)
        {
            if(i != ind && nums[i] == nums[i - 1])    continue;
            temp.add(nums[i]);
            findSubsets(result, temp, nums, i + 1);
            temp.remove(temp.size() - 1);
        }
    }
}