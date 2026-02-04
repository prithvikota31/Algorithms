class Solution {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> trackingList = new ArrayList<>();
        Arrays.sort(candidates);
        dfsCombinations(candidates, target, result, 0, trackingList);

        return result;
    }

    public void dfsCombinations(int[] candidates, int target, List<List<Integer>> result, 
    int index, List<Integer> trackingList)
    {
        if(target == 0)
        {
            result.add(new ArrayList<>(trackingList));
        }

        for(int i = index; i < candidates.length; i++)
        {
            if(i != index && candidates[i] == candidates[i - 1])
            {
                continue;
            }

            if(candidates[i] > target)
            {
                break;
            }

            trackingList.add(candidates[i]);
            dfsCombinations(candidates, target - candidates[i], result, i + 1, trackingList);
            trackingList.remove(trackingList.size() - 1);
        }
    }
}