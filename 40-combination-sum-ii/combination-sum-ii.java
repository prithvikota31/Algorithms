class Solution {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> tracingList = new ArrayList<>();

        findC(result, tracingList, target, candidates, 0);
        return result;

    }

    public void findC( List<List<Integer>> result, List<Integer> tracingList, int target,
                        int[] candidates, int ind)
    {
        if(target == 0)
        {
            result.add(new ArrayList<>(tracingList));
        }
        if(target < 0)
        {
            return;
        }
        for(int i = ind; i < candidates.length; i++)
        {
            // 1, 1, 1, 1, 2, 2, 2
            if(i > ind && candidates[i] == candidates[i - 1])    continue;
            tracingList.add(candidates[i]);
            findC(result, tracingList, target - candidates[i], candidates, i + 1);
            tracingList.remove(tracingList.size() - 1);
        }
    }
    
}