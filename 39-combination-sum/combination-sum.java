class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> trackingList = new ArrayList<>();

        combinationSum(candidates, result, 0, candidates.length, 0, target, trackingList);        
        return result;
    }

    public void combinationSum(int[] candidates, List<List<Integer>> result, int index, int n,
     int sum, int target, List<Integer> trackingList)
    {
        if(sum > target)    return;
        if(sum == target)
        {
            result.add(new ArrayList<>(trackingList));
            return;
        }
        if(index == n)
        {
            return;
        }

        trackingList.add(candidates[index]);
        combinationSum(candidates, result, index, candidates.length,
                     sum + candidates[index], target, trackingList);  
              
        trackingList.remove(trackingList.size() - 1);
        combinationSum(candidates, result, index + 1, candidates.length,
                     sum, target, trackingList); 
    }
}