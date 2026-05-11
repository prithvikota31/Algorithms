class Solution {
    public int[] separateDigits(int[] nums) {
        List<Integer> result = new ArrayList<>();

        for(int num: nums)
        {
            getDigits(num, result);
        }

        int[] ans = new int[result.size()];

        for(int i = 0; i < result.size(); i++) {
            ans[i] = result.get(i);
        }

        return ans;
    
    }


    public void getDigits(int num, List<Integer> result )
    {
        List<Integer> temp = new ArrayList<>();
        for(char c: String.valueOf(num).toCharArray())
        {
            result.add(c - '0');
        }

    }
}