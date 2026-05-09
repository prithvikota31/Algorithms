class Solution {

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {

        Arrays.sort(candidates);

        List<List<Integer>> result = new ArrayList<>();

        findCombinations(candidates,
                         target,
                         0,
                         new ArrayList<>(),
                         result);

        return result;
    }

    public void findCombinations(int[] candidates,
                                 int target,
                                 int ind,
                                 List<Integer> curr,
                                 List<List<Integer>> result)
    {
        // base case
        if(target == 0)
        {
            result.add(new ArrayList<>(curr));
            return;
        }

        if(ind >= candidates.length || target < 0)
        {
            return;
        }

        // ---------------- PICK ----------------

        curr.add(candidates[ind]);

        findCombinations(candidates,
                         target - candidates[ind],
                         ind + 1,
                         curr,
                         result);

        curr.remove(curr.size() - 1);

        // ------------ NOT PICK ----------------

        // skip duplicates
        while(ind + 1 < candidates.length &&
              candidates[ind] == candidates[ind + 1])
        {
            ind++;
        }

        findCombinations(candidates,
                         target,
                         ind + 1,
                         curr,
                         result);
    }
}