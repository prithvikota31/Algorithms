class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        int n = candidates.length;
        gatherLists(candidates, target, 0, ans, temp);
        return ans;
    }

    private void gatherLists(int[] candidates, int target, int ind,
             List<List<Integer>> ans, List<Integer> temp)
    {
        if(target < 0)
        {
            return;
        }
        if(target == 0)
        {
            ans.add(new ArrayList<>(temp));
            return;
        }

        for(int i = ind; i < candidates.length; i++)
        {
            temp.add(candidates[i]);
            gatherLists(candidates, target - candidates[i], i, ans, temp);
            temp.remove(temp.size() - 1);
        }
    }
}