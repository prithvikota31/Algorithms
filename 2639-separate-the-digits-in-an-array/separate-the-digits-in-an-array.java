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
        while(num != 0)
        {
            temp.add(num % 10);
            num = num / 10;
        }

       Collections.reverse(temp);
        result.addAll(temp);

    }
}