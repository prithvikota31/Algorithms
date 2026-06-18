class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();

        // path stores the current combination we are building.
        backtrack(candidates, target, 0, new ArrayList<>(), ans);

        return ans;
    }

    private void backtrack(
        int[] candidates,
        int remaining,
        int start,
        List<Integer> path,
        List<List<Integer>> ans
    ) {
        // If remaining becomes 0, current path is a valid combination.
        if (remaining == 0) {
            ans.add(new ArrayList<>(path));
            return;
        }

        // Try every candidate from start onward.
        // start prevents going backward and creating duplicate permutations.
        for (int i = start; i < candidates.length; i++) {
            int num = candidates[i];

            // If num is too large, this choice cannot work.
            if (num > remaining) {
                continue;
            }

            // Choose current number.
            path.add(num);

            // Recurse with same i because the same number can be reused.
            backtrack(candidates, remaining - num, i, path, ans);

            // Undo the choice so we can try the next candidate.
            path.remove(path.size() - 1);
        }
    }
}