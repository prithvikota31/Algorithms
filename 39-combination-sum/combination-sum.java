class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();

        getCombinations(ans, new ArrayList<>(), 0, target, candidates);
        return ans;
    }
    private void getCombinations(List<List<Integer>> ans, List<Integer> trackingList, int start, int target, int[] candidates)
    {
        if(target == 0)
        {
            ans.add(new ArrayList<>(trackingList));
            return;
        }

        for(int i = start; i < candidates.length; i++)
        {
            int num = candidates[i];
            if( num > target)  continue;
            trackingList.add(num);
            getCombinations(ans, trackingList, i, target - num, candidates);
            trackingList.remove(trackingList.size() - 1);
        }
    }
}