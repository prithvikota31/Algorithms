class Solution {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();

        findCombinations(
            result,
            candidates,
            target,
            temp,
            candidates.length - 1
        );

        return result;
    }

    private void findCombinations(
            List<List<Integer>> result,
            int[] candidates,
            int target,
            List<Integer> temp,
            int index) {

        if (target == 0) {
            result.add(new ArrayList<>(temp));
            return;
        }

        if (target < 0 || index < 0) {
            return;
        }

        // TAKE current element
        temp.add(candidates[index]);

        findCombinations(
            result,
            candidates,
            target - candidates[index],
            temp,
            index - 1
        );

        temp.remove(temp.size() - 1);

        // NOT TAKE current value
        // Skip all duplicates of candidates[index]
        int nextIndex = index - 1;

        while (nextIndex >= 0 &&
               candidates[nextIndex] == candidates[index]) {
            nextIndex--;
        }

        findCombinations(
            result,
            candidates,
            target,
            temp,
            nextIndex
        );
    }
}