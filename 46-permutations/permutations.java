class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        findPermutations(nums, result, 0);
        return result;
    }

    public void findPermutations(int[] nums, List<List<Integer>> result, int ind)
    {
        if(ind == nums.length)
        {
            List<Integer> perm = new ArrayList<>();
            for(int i = 0; i < nums.length; i++)
            {
                perm.add(nums[i]);
            }

            result.add(perm);
            return;
        }

        for(int i = ind; i < nums.length; i++)
        {
            swap(nums, i, ind);
            findPermutations(nums, result, ind + 1);
            swap(nums, i, ind);
        }
    }

    public void swap(int[] nums, int i, int j)
    {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}